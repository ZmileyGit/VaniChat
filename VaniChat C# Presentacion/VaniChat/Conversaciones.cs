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
    public partial class Conversaciones : Form
    {
        private ClientConnection connection;

        public Conversaciones()
        {
            InitializeComponent();
        }

        public Conversaciones(ClientConnection connection)
        {
            InitializeComponent();
            this.connection = connection;
            label1.Text = this.connection.username;
            //this.connection.requestActive();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            EstablecerSesion s = new EstablecerSesion(connection,this);
            s.ShowDialog();
        }
    }
}
