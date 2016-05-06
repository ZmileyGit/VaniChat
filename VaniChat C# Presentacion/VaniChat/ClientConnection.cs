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
        private Socket listener;
        private EndPoint endP;
        private ListBox list;
        public List<ChatC> chatList = new List<ChatC>();

        private Dictionary<int, ListBox> chats = new Dictionary<int, ListBox>();
        private ListBox messageList;

        public int id { get; private set; }
        public string username { get; private set; }

        private readonly string IP = "127.0.0.1";
        private readonly int port;

        private Thread listenerThread;
        private Thread userThread;

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
            while (connection.Available < bytes)
            {
                Thread.Sleep(200);
            }
        }

        private void listenerWait(int bytes)
        {
            while (listener.Available < bytes)
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
            s.Connect(endP);
            return s;
        }

        public int Connect(string username, out bool valid)
        {
            connection.Connect(endP);
            Data data = new Data(LOGIN, username);
            connection.Send(data.AsByteArray(true));
            this.username = username;
            wait(4);
            SizeBuffer buffer = new SizeBuffer();
            connection.Receive(buffer, 4, SocketFlags.None);
            DataBuffer databuffer = new DataBuffer(buffer.Value);
            wait(buffer.Value);
            connection.Receive(databuffer, buffer.Value, SocketFlags.None);
            id = BigEndianInt(BitConverter.ToInt32(databuffer.Data.contenido, 1));
            Console.WriteLine(id);
            valid = true;
            connection.Close();
            userlink(out valid);
            chatList = new List<ChatC>();

            return id;
        }

        public void bindList(ListBox listbox)
        {
            this.list = listbox;
        }

        public void userlink(out bool valid)
        {
            Initialize();
            connection.Connect(endP);
            Data data = new Data(USERLINK, id);
            connection.Send(data.AsByteArray(true));
            wait(4);
            SizeBuffer buffer = new SizeBuffer();
            connection.Receive(buffer, 4, SocketFlags.None);
            DataBuffer databuffer = new DataBuffer(buffer.Value);
            wait(buffer.Value);
            connection.Receive(databuffer, buffer.Value, SocketFlags.None);
            byte response = databuffer.Data.contenido[1];
            if (response > 0)
            {

                valid = false;
            } else
            {

                valid = true;
                startUserLink();
            }
            //connection.Close();
        }

        public void send(string text)
        {
            lock (listener)
            {
                Data data = new Data(MESSAGE,text);
                listener.Send(data.AsByteArray(true));
            }

        }

        public void recuperarConversacion(string hashID)
        {
            lock (connection)
            {
                Data data = new Data(RECOVERCONV, hashID);
                connection.Send(data.AsByteArray(true));
                byte[] buffer = new byte[4];
                connection.Receive(buffer, 4, SocketFlags.None);

            }
        }

        public void startUserLink()
        {
            userThread = new Thread(userListener);
            userThread.Start();
        }

        private void userListener()
        {
            try
            {
                while (true)
                {
                    wait(4);
                    lock (connection)
                    {
                        SizeBuffer buffer = new SizeBuffer();
                        connection.Receive(buffer, 4, SocketFlags.None);
                        DataBuffer databuffer = new DataBuffer(buffer.Value);
                        wait(buffer.Value);
                        connection.Receive(databuffer, buffer.Value, SocketFlags.None);
                        int id = BigEndianInt(BitConverter.ToInt32(databuffer.Data.contenido, 1));
                        byte[] b = new byte[databuffer.Data.contenido.Length - 5];
                        Array.Copy(databuffer.Data.contenido, 5, b, 0, b.Length);
                        string users = DEFAULT_CHARSET.GetString(b);
                        ChatC c = new ChatC(id, users);
                        lock (chatList)
                        {
                            chatList.Add(c);
                        }
                    }

                }
            }
            catch (Exception e)
            {

            }
        }

        public int sessionLink(string user)
        {
            try
            {
                //Initialize();
                //connection.Connect(endP);
                int cid = -1;
                lock (connection)
                {
                    Data data = new Data(GROUP, user);
                    connection.Send(data.AsByteArray(true));
                    wait(4);
                    SizeBuffer buffer = new SizeBuffer();
                    connection.Receive(buffer, 4, SocketFlags.None);
                    DataBuffer databuffer = new DataBuffer(buffer.Value);
                    wait(buffer.Value);
                    connection.Receive(databuffer, buffer.Value, SocketFlags.None);
                    cid = BigEndianInt(BitConverter.ToInt32(databuffer.Data.contenido, 1));
                    Console.WriteLine(cid);
                }
                if (cid >= 0)
                {
                    bool b = chatLink(cid);
                    if (b)
                    {
                        ChatC c = new ChatC(cid, user);
                        lock (chatList)
                        {
                            chatList.Add(c);
                        }
                        list.Items.Add(c);
                    }
                    return cid;
                }
                //connection.Close();
                return cid;
            } catch (Exception e)
            {
                return -1;
            }
        }

        public bool chatLink(int chatId)
        {
            listener = InitializeSocket();
            byte response = 1;
            lock (listener)
            {
                Data data = new Data(CHATLINK, chatId, id);
                listener.Send(data.AsByteArray(true));
                listenerWait(4);
                SizeBuffer buffer = new SizeBuffer();
                listener.Receive(buffer, 4, SocketFlags.None);
                DataBuffer databuffer = new DataBuffer(buffer.Value);
                listenerWait(buffer.Value);
                listener.Receive(databuffer, buffer.Value, SocketFlags.None);
                response = databuffer.Data.contenido[1];
            }
            if (response > 0)
            {
                return false;
            }
            else
            {
                beginReceiver(chatId);
                return true;
            }
        }
        public void CloseChat()
        {
            if (listenerThread != null)
            {
                listenerThread.Abort();
            }
            try
            {
                listener.Close();
            }catch(Exception e)
            {

            }
        }


        public void Close()
        {
            if (listenerThread != null)
            {
                listenerThread.Abort();
            }
            if(userThread != null)
            {
                userThread.Abort();
            }
            try
            {
                connection.Close();
                listener.Close();
            }catch(Exception e)
            {

            }
        }

        public void registerRoom(int chat, ListBox listBox)
        {
            chats.Add(chat, listBox);
        }

        public void beginReceiver(int chatId)
        {
            listenerThread = new Thread(receive);
            listenerThread.Start();
        }

        private void receive() //Recibir mensajes en chat
        {
            try {
                while (listener.Connected)
                {
                    listenerWait(4);
                    lock (listener)
                    {
                        SizeBuffer buffer = new SizeBuffer();
                        listener.Receive(buffer, 4, SocketFlags.None);
                        DataBuffer databuffer = new DataBuffer(buffer.Value);
                        listenerWait(buffer.Value);
                        listener.Receive(databuffer, buffer.Value, SocketFlags.None);
                        int chatId = BigEndianInt(BitConverter.ToInt32(databuffer.Data.contenido, 1));
                        int us = BigEndianInt(BitConverter.ToInt32(databuffer.Data.contenido, 5));
                        byte[] u = new byte[us];
                        Array.Copy(databuffer.Data.contenido, 1 + 4 + 4, u, 0, u.Length);
                        string user = DEFAULT_CHARSET.GetString(u);
                        byte[] b = new byte[buffer.Value - us - 1 - 4 - 4];
                        Array.Copy(databuffer.Data.contenido, 1+4+4+us, b, 0, b.Length);
                        string text = DEFAULT_CHARSET.GetString(b);
                        ListBox listBox = chats[chatId];
                        if (listBox != null)
                        {
                            listBox.Invoke((MethodInvoker)delegate
                            {
                                listBox.Items.Add($"{user}->{text}");
                            });
                        }
                    }

                }
            }
            catch (Exception e)
            {

            }
        }
    }
}
