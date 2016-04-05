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

namespace VaniChat
{
    public class ClientConnection
    {
        private Socket connection;
        private EndPoint endP;
        public int id { get; private set; }
        public string username{ get; private set; }

        private readonly string IP = "127.0.0.1";
        private readonly int port = 1234;

        private Thread thread;

        public ClientConnection()
        {
            connection = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            endP = new IPEndPoint(IPAddress.Parse(IP), port);
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
            connection.Send(BitConverter.GetBytes((byte)0));
            byte[] b = Encoding.Unicode.GetBytes(username);
            connection.Send(BitConverter.GetBytes((short)b.Length));
            connection.Send(b);
            
            //byte[] buffer = new byte[4];
            //Thread.Sleep(500);
            //connection.Receive(buffer);
            //Thread.Sleep(500);
            //buffer.Reverse(); //Necesitamos que se actualice bien la referencia asi que esto no hace nada XD
            //Array.Reverse(buffer);
            //id = BitConverter.ToInt32(buffer,0);
            this.username = username;
            Console.WriteLine(id);
            connection.Close();
            return id;
        }

        public void send(string text)
        {
            Initialize();
            connection.Connect(endP);
            byte[] b = Encoding.Unicode.GetBytes(text);
            /*byte[] todo = new byte[1 + 4 + 4 + b.Length];
            todo[0] = 1;
            //byte[] i = BitConverter.GetBytes(1);
            Array.Reverse(i);
            Array.Copy(i, 0, todo, 1, 4);
            i = BitConverter.GetBytes(b.Length);
            Array.Reverse(i);
            Array.Copy(i, 0, todo, 5, 4);
            Array.Copy(b, 0, todo, 9, b.Length);*/
            MemoryStream ms = new MemoryStream(1 + 4 + 4 + b.Length);
            BinaryWriter bw = new BinaryWriter(ms);
            bw.Write((byte)1);
            byte[] i = BitConverter.GetBytes(1);
            Array.Reverse(i);
            bw.Write(i);
            bw.Write((byte)((b.Length >> 24 ) & 255));
            bw.Write((byte)((b.Length >> 16) & 255));
            bw.Write((byte)((b.Length >> 8) & 255));
            bw.Write((byte)((b.Length) & 255));
            bw.Write(b);
            bw.Close();
            connection.Send(ms.ToArray());
            //connection.Send(BitConverter.GetBytes((byte)1));
            //byte[] i = BitConverter.GetBytes((int)1);
            //Array.Reverse(i);
            //connection.Send(i);
            //i = BitConverter.GetBytes(b.Length);
            //Array.Reverse(i);
            //connection.Send(i);
            //connection.Send(b);
            //connection.Send(todo);
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
            Initialize();
            connection.Connect(endP);
            connection.Send(BitConverter.GetBytes((byte)2));
            byte[] b = Encoding.Unicode.GetBytes(user);
            connection.Send(BitConverter.GetBytes((short)b.Length));
            connection.Send(b);
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
            Initialize();
            connection.Connect(endP);
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
                    buffer.Reverse(); //Necesitamos que se actualice bien la referencia asi que esto no hace nada XD
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
