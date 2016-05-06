using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using static VaniChat.Util;

namespace VaniChat
{
    public partial class Buffer
    {
        private int size { get; set; }
        public byte[] buffer { get; private set; }

        public Buffer(int size)
        {
            buffer = new byte[size];
            this.size = size;
        }

        public Buffer(byte[] buffer)
        {
            this.buffer = buffer;
            size = buffer.Length;
        }

        public static implicit operator byte[] (Buffer buffer)
        {
            return buffer.buffer;
        }

    }

    public class SizeBuffer : Buffer
    {
        public SizeBuffer() : base(4)
        {
            
        }

        public SizeBuffer(byte[] buffer) : base(buffer)
        {

        }

        public int Value
        {
            get
            {
                return BigEndianInt(BitConverter.ToInt32(buffer, 0));
            }
        }
    }

    public class DataBuffer : Buffer
    {
        public DataBuffer(int size) : base(size)
        {
            
        }

        public DataBuffer(byte[] buffer) : base(buffer)
        {

        }

        public Data Data
        {
            get
            {
                return Data.DataFromArray(buffer);
            }
        }
    }
}
