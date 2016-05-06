using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;

namespace VaniChat
{
    public partial class Conversaciones : Form
    {
        private ClientConnection connection;
        private Thread thread;

        public Conversaciones()
        {
            InitializeComponent();
        }

        public Conversaciones(ClientConnection connection)
        {
            InitializeComponent();
            this.connection = connection;
            label1.Text = this.connection.username;
            this.connection.bindList(listBox1);
            MessageBox.Show($"Signed in as {this.connection.username}");
            thread = new Thread(update);
            thread.Start();
            //connection.beginReceiver();
            //this.connection.requestActive();
        }

        private void update()
        {
            try
            {
                while (true)
                {
                    Thread.Sleep(8000);
                    lock (connection.chatList)
                    {
                        listBox1.Invoke((MethodInvoker)delegate
                        {
                            listBox1.Items.Clear();
                            foreach(ChatC c in connection.chatList)
                            {
                                listBox1.Items.Add(c);
                            }
                        });
                    }
                }
            }
            catch (Exception e)
            {

            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            EstablecerSesion s = new EstablecerSesion(connection,this);
            s.ShowDialog();
        }

        private void Conversaciones_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (connection != null)
            {
                connection.Close();
                MessageBox.Show("Logged out");
            }
        }
    }
}
