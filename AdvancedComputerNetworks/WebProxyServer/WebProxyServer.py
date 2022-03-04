# The program should be able to:
    # 1. Respond to HTTP & HTTPS requests and should display each request on a management
    # console. It should forward the request to the Web server and relay the response to the browser.
    # 2. Handle Websocket connections.
    # 3. Dynamically block selected URLs via the management console.
    # 4. Efficiently cache HTTP requests locally and thus save bandwidth. You must gather timing and
    # bandwidth data to prove the efficiency of your proxy.
    # 5. Handle multiple requests simultaneously by implementing a threaded server.

import sys
import socket
from threading import Thread
import ssl
# import argparse
# import os

instructions = """* Traffic will be logged to the console. The below commands are available:
    * /b <URL>   - Block the URL
    * /u <URL>   - Unblock the URL
    * /lb        - List blocked URLs
    * /c         - List cached URLs
    * /q (or ^C) - Quit the program
    * /h         - Display this help message again"""

class Proxy:
    def __init__(self, port=12345):
        self.port = port
        self.proxy = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.proxy.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.buffer_size = 4096
        self.blocked_URLS = []
        self.cached_sites = {}

    def run(self):
        self.proxy.bind(("0.0.0.0", self.port))
        self.proxy.listen(100)
        print("- Proxy server is running on port {}".format(self.port))
        print(instructions)
        while True:
            try:
                client, addr = self.proxy.accept()
                print(" -- {}:{}".format(addr[0], addr[1]))
                Thread(target=self.handle_request, args=(client,)).start()
            except KeyboardInterrupt:
                print("\n- Exiting...")
                sys.exit()

    def handle_request(self, client):
        head = self.parse_head(client.recv(self.buffer_size))
        headers = head["headers"]
        request = "{}\r\n".format(head["meta"])
        for key, value in headers.items():
            request += "{}: {}\r\n".format(key, value)
        request += "\r\n"
        if "content-length" in headers:
            while len(head["chunk"]) < int(headers["content-length"]):
                head["chunk"] += client.recv(self.buffer_size)

        request = request.encode() + head["chunk"]
        port = 80
        try:
            tmp = head["meta"].split(" ")[1].split("://")[1].split("/")[0]
        except IndexError:
            client.close()
            return
        if tmp.find(":") > -1:
            port = int(tmp.split(":")[1])

        response = self.send_to_server(headers["host"], port, request)
        client.sendall(response)
        client.close()

    def send_to_server(self, host, port, data):
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.connect((socket.gethostbyname(host), port))
        server.sendall(data)

        head = self.parse_head(server.recv(4096))
        headers = head["headers"]
        response = "{}\r\n".format(head["meta"])
        for key, value in headers.items():
            response += "{}: {}\r\n".format(key, value)
        response += "\r\n"

        if "content-length" in headers:
            while len(head["chunk"]) < int(headers["content-length"]):
                head["chunk"] += server.recv(self.buffer_size)

        response = response.encode() + head["chunk"]
        server.close()
        return response

    def parse_head(self, head_request):
        nodes = head_request.split(b"\r\n\r\n")
        heads = nodes[0].split(b"\r\n")
        meta = heads.pop(0).decode("utf-8")
        data = {
            "meta": meta,
            "headers": {},
            "chunk": b""
        }

        if len(nodes) >= 2:
            data["chunk"] = nodes[1]

        for head in heads:
            pieces = head.split(b": ")
            key = pieces.pop(0).decode("utf-8")
            if key.startswith("Connection: "):
                data["headers"][key.lower()] = "close"
            else:
                data["headers"][key.lower()] = b": ".join(pieces).decode("utf-8")
        return data

    def block_url(self, url):
        self.blocked_URLS.append(url)
    
    def unblock_url(self, url):
        self.blocked_URLS.remove(url)

    def list_blocked_urls(self):
        print("- Blocked URLs:")
        for url in self.blocked_URLS:
            print("     - {}".format(url))
    
    def list_cached_urls(self):
        print("- Cached URLs:")
        for url in self.cached_sites:
            print("     - {}".format(url))

    def block_url_from_console(self):
        url = input("- Enter URL to block: ")
        self.block_url(url)
    


if __name__ == "__main__":
    print("\n- Starting proxy server...")
    proxy = Proxy(12345)
    proxy.run()