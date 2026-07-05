#!/bin/bash

# E-Commerce Cart Services Health Check Script
# This script validates that all services are ready for Postman testing

echo "🔍 Checking E-Commerce Cart Services Health..."
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Service endpoints
API_GATEWAY="http://localhost:8080"
EUREKA_SERVER="http://localhost:8761"

# Function to check service health
check_service() {
    local service_name=$1
    local url=$2
    local expected_text=$3
    
    echo -n "Checking $service_name... "
    
    if response=$(curl -s -m 10 "$url" 2>/dev/null); then
        if [[ -n "$expected_text" ]] && [[ "$response" == *"$expected_text"* ]]; then
            echo -e "${GREEN}✅ HEALTHY${NC}"
            return 0
        elif [[ -z "$expected_text" ]] && [[ -n "$response" ]]; then
            echo -e "${GREEN}✅ RESPONDING${NC}"
            return 0
        else
            echo -e "${YELLOW}⚠️  UNEXPECTED RESPONSE${NC}"
            echo "   Response: ${response:0:100}..."
            return 1
        fi
    else
        echo -e "${RED}❌ NOT RESPONDING${NC}"
        return 1
    fi
}

# Function to check if port is in use
check_port() {
    local port=$1
    local service_name=$2
    
    if nc -z localhost $port 2>/dev/null; then
        echo -e "Port $port ($service_name): ${GREEN}✅ OPEN${NC}"
        return 0
    else
        echo -e "Port $port ($service_name): ${RED}❌ CLOSED${NC}"
        return 1
    fi
}

echo "🌐 Checking Network Ports..."
echo "------------------------------"
check_port 8080 "API Gateway"
check_port 8761 "Eureka Server"

echo ""
echo "🏥 Checking Service Health..."
echo "------------------------------"
check_service "API Gateway" "$API_GATEWAY/health" "API Gateway"
check_service "API Gateway Welcome" "$API_GATEWAY/" "Welcome"
check_service "Eureka Server" "$EUREKA_SERVER/actuator/health" ""

echo ""
echo "🔍 Checking Service Registration..."
echo "------------------------------------"
echo -n "Checking registered services in Eureka... "

if eureka_response=$(curl -s -m 10 "$EUREKA_SERVER/eureka/apps" 2>/dev/null); then
    service_count=$(echo "$eureka_response" | grep -o "<name>[^<]*</name>" | wc -l)
    if [ "$service_count" -gt 0 ]; then
        echo -e "${GREEN}✅ $service_count services registered${NC}"
        
        # List registered services
        echo "   Registered services:"
        echo "$eureka_response" | grep -o "<name>[^<]*</name>" | sed 's/<name>//g' | sed 's/<\/name>//g' | sed 's/^/   - /'
    else
        echo -e "${YELLOW}⚠️  No services registered${NC}"
    fi
else
    echo -e "${RED}❌ Cannot reach Eureka${NC}"
fi

echo ""
echo "🧪 Testing Authentication Endpoints..."
echo "---------------------------------------"

# Test public endpoint (should work)
echo -n "Testing public endpoint (/health)... "
if curl -s -m 10 "$API_GATEWAY/health" >/dev/null 2>&1; then
    echo -e "${GREEN}✅ ACCESSIBLE${NC}"
else
    echo -e "${RED}❌ FAILED${NC}"
fi

# Test protected endpoint without auth (should return 401)
echo -n "Testing protected endpoint without auth... "
if response=$(curl -s -m 10 -w "%{http_code}" "$API_GATEWAY/balance/1" 2>/dev/null); then
    http_code="${response: -3}"
    if [ "$http_code" = "401" ]; then
        echo -e "${GREEN}✅ PROPERLY BLOCKED (401)${NC}"
    else
        echo -e "${YELLOW}⚠️  Got $http_code (expected 401)${NC}"
    fi
else
    echo -e "${RED}❌ NO RESPONSE${NC}"
fi

echo ""
echo "📋 Test Results Summary"
echo "========================"

# Count successful checks
gateway_health="FAIL"
if curl -s -m 5 "$API_GATEWAY/health" >/dev/null 2>&1; then
    gateway_health="OK"
fi

eureka_health="FAIL"
if curl -s -m 5 "$EUREKA_SERVER/actuator/health" >/dev/null 2>&1; then
    eureka_health="OK"
fi

if [ "$gateway_health" = "OK" ] && [ "$eureka_health" = "OK" ]; then
    echo -e "${GREEN}🎉 All core services are healthy!${NC}"
    echo ""
    echo "✅ Ready for Postman testing!"
    echo ""
    echo "📝 Next Steps:"
    echo "   1. Import Postman collections from /postman directory"
    echo "   2. Import the environment file: Ecommerce-Cart-Local-Environment.json"
    echo "   3. Run the 'E-Commerce Cart - Automated Test Runner' collection"
    echo "      (Or manually run the requests in order)"
    echo "   To run automatically from CLI if newman is installed:"
    echo "   newman run postman/Ecommerce-Cart-Test-Runner.postman_collection.json \\"
    echo "          -e postman/Ecommerce-Cart-Local-Environment.json"
    
    exit 0
else
    echo -e "${RED}❌ Some services are not healthy${NC}"
    echo ""
    echo "🔧 Troubleshooting:"
    echo "   1. Ensure Docker Compose is running: docker-compose up -d"
    echo "   2. Check logs: docker-compose logs [service-name]"
    echo "   3. Verify all containers are up: docker-compose ps"
    echo "   4. Check for port conflicts"
    
    exit 1
fi
