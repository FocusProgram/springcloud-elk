docker-compose -f kafka-manager.yml stop

docker-compose -f kafka-manager.yml rm --force

docker-compose -f kafka-manager.yml up -d
