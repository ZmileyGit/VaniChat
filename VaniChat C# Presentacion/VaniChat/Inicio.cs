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
    public partial class Inicio : Form
    {

        private readonly string IP = "127.0.0.1";
        private readonly int port = 8080;

        private ClientConnection connection;

        public Inicio()
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
                Conversaciones c = new Conversaciones(connection);
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
