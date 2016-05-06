using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using static VaniChat.Util;

namespace VaniChat
{
    public class Data
    {
        private int length { get; set; }
        public byte mode { get; private set; }
        private string texto;
        public string Texto {
            get
            {
                return texto;
            }
            set
            {
                texto = value;
                contenido = Configuration.DEFAULT_CHARSET.GetBytes(texto);
            }
        }
        public byte[] contenido { get; set; }
        
        public Data(byte mode, byte[] contenido)
        {
            this.mode = mode;
            this.contenido = contenido;
        }

        public Data(byte mode, string texto)
        {
            this.mode = mode;
            Texto = texto;
        }

        public byte[] AsByteArray(bool bigEndian = false)
        {
            MemoryStream ms = new MemoryStream();
            BinaryWriter bw = new BinaryWriter(ms);
            if (bigEndian)
            {
                bw.Write(BigEndianInt(contenido.Length + 1));
            }else
            {
                bw.Write(contenido.Length + 1);
            }
            bw.Write(mode);
            bw.Write(contenido);
            bw.Close();
            byte[] b = ms.ToArray();
            ms.Close();
            return b;
        }

        public static Data DataFromArray(byte[] data)
        {
            if (data.Length > 0)
            {
                ArraySegment<byte> d2 = new ArraySegment<byte>(data, 1, data.Length - 1);
                Data d = new Data(data[0], d2.Array);
                return d;
            }
            else
            {
                return null;
            }
            
        }


    }
}
