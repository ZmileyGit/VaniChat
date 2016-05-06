using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Windows.Forms;
using System.Threading;
using System.Diagnostics;
using System.IO;

using static VaniChat.Configuration;
using static VaniChat.Util;

namespace VaniChat
{
    public class ClientConnection
    {
        private Socket connection;
        private EndPoint endP;
        public int id { get; private set; }
        public string username{ get; private set; }

        private readonly string IP = "127.0.0.1";
        private readonly int port;

        private Thread thread;

        public ClientConnection()
        {
            this.port = Configuration.DEFAULT_PORT;
            Initialize();
        }

        public ClientConnection(int port)
        {
            this.port = port;
            Initialize();
        }

        private void wait(int bytes)
        {
            while(connection.Available < bytes)
            {
                Thread.Sleep(200);
            }
        }

        private void Initialize()
        {
            connection = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            endP = new IPEndPoint(IPAddress.Parse(IP), port);
        }

        private Socket InitializeSocket()
        {
            Socket s = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            endP = new IPEndPoint(IPAddress.Parse(IP), port);
            return s;
        }

        public int Connect(string username)
        {
            connection.Connect(endP);
            Data data = new Data(LOGIN, username);
            connection.Send(data.AsByteArray(true));
            this.username = username;
            wait(4);
            SizeBuffer buffer = new SizeBuffer();
            connection.Receive(buffer,4,SocketFlags.None);
            DataBuffer databuffer = new DataBuffer(buffer.Value);
            wait(buffer.Value);
            connection.Receive(databuffer, buffer.Value, SocketFlags.None);
            id = BigEndianInt(BitConverter.ToInt32(databuffer.Data.contenido, 1));
            Console.WriteLine(id);

            //connection.Close();
            return id;
        }

        public void send(string text)
        {
            //Initialize();
            //connection.Connect(endP);
            Data data = new Data(MESSAGE, text);
            connection.Send(data.AsByteArray());
            //connection.Close();
            
        }

        public void recuperarConversacion(string hashID)
        {
            // Initialize();
            //connection.Connect(endP);
            Data data = new Data(RECOVERCONV, hashID);
            connection.Send(data.AsByteArray());
            byte[] buffer = new byte[4];
            connection.Receive(buffer, 4, SocketFlags.None);

            connection.Close();
        }

        public void requestActive()
        {
            //Obtener conversaciones Activas
            //connection.Send(BitConverter.GetBytes((byte)3));
            //byte[] b = Encoding.Unicode.GetBytes();
            //connection.Send(BitConverter.GetBytes((short)b.Length));
            //connection.Send(b);
        }

        public void sessionLink(string user)
        {
            //SessionPacketHandler
            //Initialize();
            //connection.Connect(endP);
            Data data = new Data(GROUP, user);
            connection.Send(data.AsByteArray());
            connection.Close();
        }

        public void Close()
        {
            if (thread != null)
            {
                thread.Abort();
            }
            connection.Close();
        }

        public void beginReceiver(ListBox listBox)
        {
            //Initialize();
            //connection.Connect(endP);
            thread = new Thread(receive);
            thread.Start(listBox);
        }

        private void receive(object o) //Recibir mensajes en chat
        {
            try {
                while (connection.Connected)
                {
                    byte[] buffer = new byte[1];
                    connection.Receive(buffer, 1, SocketFlags.None);
                    byte type = buffer[0];
                    buffer = new byte[2];
                    Thread.Sleep(500);
                    connection.Receive(buffer, 2, SocketFlags.None);
                    Thread.Sleep(500);
                    buffer.Reverse();
                    int size = BitConverter.ToInt16(buffer, 0);
                    buffer = new byte[size];
                    Thread.Sleep(500);
                    connection.Receive(buffer, size, SocketFlags.None);
                    Thread.Sleep(500);
                    buffer.Reverse();
                    string text = Encoding.Unicode.GetString(buffer);
                    ListBox listBox = (ListBox)o;
                    listBox.Invoke((MethodInvoker)delegate
                    {
                        listBox.Items.Add(text);
                    });
                }
            }catch(Exception e)
            {

            }
        }
    }
}
