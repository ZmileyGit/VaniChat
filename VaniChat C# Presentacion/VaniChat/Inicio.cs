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
        }

        private void button1_Click(object sender, EventArgs e)
        {

            connection = new ClientConnection();
            try {
                int id = connection.Connect(textBox1.Text);
                Conversaciones c = new Conversaciones(connection);
                this.Visible = false;
                c.ShowDialog();
                this.Visible = true;
                this.connection = null;
            }catch(Exception ex)
            {
                Console.WriteLine(ex.Message);
                MessageBox.Show("Error");
            }

        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (connection != null)
            {
                connection.Close();
            }
        }
    }
}
