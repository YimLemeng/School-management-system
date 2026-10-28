#!/bin/bash
set -e

echo "🚀 Pulling latest code..."
git pull origin main

echo "🔨 Rebuilding and restarting containers..."
docker compose up -d --build

echo "🧹 Cleaning unused images..."
docker image prune -f

echo "✅ Deployment Finished!"
