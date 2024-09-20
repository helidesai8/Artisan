#!/bin/sh
cd /home/student/DEPLOYMENT/
docker compose down
docker system prune -af
docker compose build
docker compose up -d