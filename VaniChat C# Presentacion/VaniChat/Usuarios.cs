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
    public partial class Usuarios : Form
    {
        private ClientConnection connection;

        public Usuarios()
        {
            InitializeComponent();
        }

        public Usuarios(ClientConnection connection)
        {
            InitializeComponent();
            this.connection = connection;
            label1.Text = this.connection.username;
            //this.connection.requestUsers();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if(listBox1.SelectedIndex != -1)
            {

            }
        }
    }
}
