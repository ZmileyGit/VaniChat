using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Windows.Forms;
using System.Threading;

namespace VaniChat
{
    public class ClientConnection
    {
        private Socket connection;
        private EndPoint endP;
        public int id { get; private set; }
        public string username{ get; private set; }

        private Thread thread;

        public ClientConnection(Socket connection, EndPoint endP)
        {
            this.connection = connection;
            this.endP = endP;
        }

        public int Connect(string username)
        {
            connection.Connect(endP);
            connection.Send(BitConverter.GetBytes((byte)0));
            byte[] b = Encoding.Unicode.GetBytes(username);
            connection.Send(BitConverter.GetBytes((short)b.Length));
            connection.Send(b);
            
            byte[] buffer = new byte[4];
            Thread.Sleep(500);
            connection.Receive(buffer);
            Thread.Sleep(500);
            buffer.Reverse(); //Necesitamos que se actualice bien la referencia asi que esto no hace nada XD
            Array.Reverse(buffer);
            id = BitConverter.ToInt32(buffer,0);
            this.username = username;
            Console.WriteLine(id);
            return id;
        }

        public void send(string text)
        {
            connection.Send(BitConverter.GetBytes((byte)1));
            byte[] b = Encoding.Unicode.GetBytes(text);
            connection.Send(BitConverter.GetBytes((short)b.Length));
            connection.Send(b);
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
            connection.Send(BitConverter.GetBytes((byte)2));
            byte[] b = Encoding.Unicode.GetBytes($"{username}:{user}");
            connection.Send(BitConverter.GetBytes((short)b.Length));
            connection.Send(b);
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
