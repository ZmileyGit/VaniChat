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
    public partial class EstablecerSesion : Form
    {
        private Form prev;
        private ClientConnection connection;

        public EstablecerSesion()
        {
            InitializeComponent();
        }

        public EstablecerSesion(ClientConnection connection, Form prev)
        {
            InitializeComponent();
            this.connection = connection;
            this.prev = prev;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if(connection != null)
            {
                connection.sessionLink(textBox1.Text);
                prev.Visible = false;
                this.Visible = false;
                Chat c = new Chat(connection);
                c.ShowDialog();
                prev.Visible = true;
                this.Close();
            }
        }
    }
}
