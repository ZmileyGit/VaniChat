using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Net.Sockets;
using System.Net;

namespace VaniChat
{
    public partial class Form1 : Form
    {

        public readonly string IP = "127.0.0.1";
        public readonly int port = 8080;

        private ClientConnection connection;

        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Socket socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            IPEndPoint endP = new IPEndPoint(IPAddress.Parse(IP), port);
            connection = new ClientConnection(socket, endP);
            try {
                int id = connection.Connect(textBox1.Text);
                if (id != 0)
                {
                    MessageBox.Show("Error");
                }

                Usuarios c = new Usuarios(connection);
                this.Visible = false;
                c.ShowDialog();
                this.Visible = true;
            }catch(Exception ex)
            {
                MessageBox.Show("Error");
            }

        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            connection.Close();
        }
    }
}
