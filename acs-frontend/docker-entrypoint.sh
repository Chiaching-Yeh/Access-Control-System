#!/bin/sh
echo "Injecting runtime environment variables..."

: "${API_URL:=http://localhost:8080}"  # 預設值（可 override）

cat /usr/share/nginx/html/assets/env.template.js | \
  sed "s|__API_URL__|${API_URL}|g" > /usr/share/nginx/html/assets/env.js

echo "Starting Nginx..."

exec "$@"
