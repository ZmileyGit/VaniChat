﻿using System;
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

        public Chat(ClientConnection connection)
        {
            InitializeComponent();
            this.connection = connection;
            this.connection.beginReceiver(listBox1);
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.connection.send(textBox1.Text);
        }
    }
}
