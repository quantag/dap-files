call set-env

curl -X POST -d "{\"file\":\"892df0dd-b14f-405c-999b-a582b038a16f.png\"}" -k  %BASE_URL%/getFile -H "Content-Type:application/json"