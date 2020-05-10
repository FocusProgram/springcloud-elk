docker-compose -f zookeeper.yml stop

docker-compose -f zookeeper.yml rm --force

docker-compose -f zookeeper.yml up -d
