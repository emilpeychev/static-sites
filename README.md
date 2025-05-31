# static-sites

```sh
docker run -d \
  --name=proxy-client \
  --hostname=proxy-client \
  --cap-add=NET_ADMIN \
  --cap-add=SYS_MODULE \
  -e PUID=1000 \
  -e PGID=1000 \
  -e TZ=Europe/Sofia \
  -e LOG_CONFS=true \
  -v /lib/modules:/lib/modules \
  --sysctl="net.ipv4.conf.all.src_valid_mark=1" \
  --add-host=host.docker.internal:172.17.0.1 \
  --restart unless-stopped \
  linuxserver/wireguard

sleep 3

docker exec -i proxy-client /bin/sh <<EOF
/usr/bin/wg genkey | tee /tmp/privatekey | /usr/bin/wg pubkey > /tmp/publickey
mkdir -p /config/wg_confs
cat <<CONFIG > /config/wg_confs/wg0.conf
[Interface]
PrivateKey = \$(cat /tmp/privatekey)
Address = 10.1.0.2/32
DNS = 1.1.1.1

[Peer]
PublicKey = <server-public-key>
Endpoint = 172.236.0.220:51820
AllowedIPs = 10.1.0.0/24
PersistentKeepalive = 25
CONFIG
chmod 600 /config/wg_confs/wg0.conf
chown 1000:1000 /config/wg_confs/wg0.conf
EOF
docker exec -it proxy-client sed -i "s|<server-public-key>|nO3f6mSQrqOmDt9qlZ6S6/UYlQNGaq20vKXQVu0NyFI=|" /config/wg_confs/wg0.conf
docker exec -it proxy-client cat /tmp/publickey
docker exec -it proxy-client cat /config/wg_confs/wg0.conf
docker network connect jenkins proxy-client
docker exec proxy-client sh -c "echo '172.17.0.1 host.docker.internal' >> /etc/hosts"

```
