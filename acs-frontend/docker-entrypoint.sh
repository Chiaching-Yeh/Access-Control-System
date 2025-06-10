set -e  # 碰到錯誤就終止 script
set -u  # 使用未定義的變數會報錯

echo "Injecting runtime environment variables..."

if [ ! -f /usr/share/nginx/html/assets/env.template.js ]; then
  echo "❌ Missing env.template.js! Did you forget to COPY the right file?" >&2
  exit 1
fi

: "${API_URL:=http://localhost:8080}"

sed "s|__API_URL__|${API_URL}|g" /usr/share/nginx/html/assets/env.template.js > /usr/share/nginx/html/assets/env.js

echo "Starting Nginx..."

exec "$@"
