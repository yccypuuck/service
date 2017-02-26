/*!
 * Cpp server service, Inspired by http://github.com/hassanyf
 * runs all the time waiting for clients to connect.
 * one client connection at a time
 * Version - 0.0.1
 */
#include <iostream>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <unistd.h>

using namespace std;

static int serverSocket, clientCount, portNum, bufsize;
static struct sockaddr_in server_addr;
static socklen_t size;

void srv_bind();
void srv_socket();
void client_close(int clientSocket);
void client_receive(int clientSocket);
int client_accept();

int main()
{
    portNum = 1500;
    bufsize = 1024;
    char buffer[bufsize];
    clientCount = 0;

    srv_socket();
    srv_bind();
    size = sizeof(server_addr);

    /* ------------- LISTENING CALL ------------- */
    /* ---------------- listen() ---------------- */
    listen(serverSocket, 1);

    while (1) {
        cout << "=> Waiting for client..." << endl;
        int clientSocket = client_accept();
//        if (clientSocket < 0) {
//            continue;
//        }
        client_receive(clientSocket);
        client_close(clientSocket);
    }

    close(serverSocket);
    return 0;
}

void srv_socket() {
    /* ---------- ESTABLISHING SOCKET CONNECTION ----------*/
    /* --------------- socket() function ------------------*/

    serverSocket = socket(AF_INET, SOCK_STREAM, 0);

    if (serverSocket < 0)
    {
        cout << "\nError establishing socket..." << endl;
        exit(4);
    }

    cout << "\n=> Socket server has been created..." << endl;

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = htons(INADDR_ANY);
    server_addr.sin_port = htons(portNum);
}

void srv_bind() {
    /* ---------- BINDING THE SOCKET ---------- */
    /* ---------------- bind() ---------------- */

    if ((bind(serverSocket, (struct sockaddr*)&server_addr,sizeof(server_addr))) < 0)
    {
        cout << "=> Error binding connection, the socket has already been established..." << endl;
        exit (4);
    }
}

int client_accept() {
    char buffer[bufsize];
    memset(buffer, 0, sizeof(buffer));

    /* ------------- ACCEPTING CLIENTS  ------------- */
    /* ----------------- accept() ------------------- */

    int clientSocket = -1;
    clientSocket = accept(serverSocket,(struct sockaddr *)&server_addr,&size);
    // first check if it is valid or not
    if (clientSocket < 0) {
        cout << "=> Error on accepting..." << endl;
        return clientSocket;
    }

    clientCount++;

    strcpy(buffer, "=> Server connected...\nMessage # from the client will end the connection. Connection OK\r");
    send(clientSocket, buffer, bufsize, 0);
    cout << "=> Connected with the client #" << clientCount << endl;
    cout << buffer << endl;

    return clientSocket;
}

void client_receive(int clientSocket) {
    char buffer[bufsize];
    bool isExit = false;

    /* -------- COMMUNICATION WITH CLIENT  -------- */
    /* ------------------ recv() ------------------ */

    do {
        cout << "Client: ";
        memset(buffer, 0, sizeof(buffer));
        recv(clientSocket, buffer, bufsize, 0);
        cout << buffer << " ";
        if (*buffer == '#') {
            isExit = true;
        }

        cout << "\nServer: " << buffer << endl;
        send(clientSocket, buffer, bufsize, 0);
    } while (!isExit);
}

void client_close(int clientSocket) {
    /* ---------------- CLOSE CALL ------------- */
    /* ----------------- close() --------------- */

    // inet_ntoa converts packet data to IP, which was taken from client
    cout << "\n\n=> Connection terminated with IP " << inet_ntoa(server_addr.sin_addr);
    close(clientSocket);
    clientCount--;
}