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

        private ClientConnection connection;

        public Inicio()
        {
            InitializeComponent();
            textBox2.Text = Configuration.DEFAULT_PORT.ToString();
            textBox2.Enabled = false;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(textBox1.Text))
            {
                return;
            }
            ushort puerto = 0;
            if (ushort.TryParse(textBox2.Text, out puerto))
            {
                connection = new ClientConnection(puerto);
                try
                {
                    bool valid = false;
                    int id = connection.Connect(textBox1.Text, out valid);
                    if (valid)
                    {
                        Conversaciones c = new Conversaciones(connection);
                        this.Visible = false;
                        c.ShowDialog();
                        this.Visible = true;
                        this.connection = null;
                    }else
                    {
                        MessageBox.Show("Error");
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.Message);
                    MessageBox.Show("Error");
                }
            }

        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (connection != null)
            {
                connection.Close();
            }
        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {
            ushort puerto = 0;
            if(!ushort.TryParse(textBox2.Text,out puerto) || string.IsNullOrWhiteSpace(textBox2.Text))
            {
                textBox2.Text = "1234";
            }
        }
    }
}
