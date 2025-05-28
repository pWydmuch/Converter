#!/bin/bash
pushd terraform
API_URL=$(tofu output -raw api_endpoint)
popd
cd front
echo "VITE_API_URL=$API_URL" > .env.production
npm run build
aws s3 sync dist/ s3://p11h-converter --delete

