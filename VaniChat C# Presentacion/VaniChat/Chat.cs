using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace VaniChat
{
    public partial class Chat : Form
    {

        private ClientConnection connection;

        public Chat()
        {
            InitializeComponent();
        }

        public Chat(ClientConnection connection, int cId)
        {
            InitializeComponent();
            this.connection = connection;
            this.connection.registerRoom(cId, listBox1);
            //this.connection.beginReceiver(cId);
            //this.connection.beginReceiver(listBox1);
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(textBox1.Text))
            {
                //listBox1.Items.Add($"{connection.username}: {textBox1.Text}");
                string text = textBox1.Text;
                textBox1.Clear();
                textBox1.Focus();
                connection.send(text);
            }
        }
    }
}
