using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace VaniChat
{
    public static class Util
    {
        public static int BigEndianInt(int i)
        {
            return (int)(((i & 0xff) << 24) + ((i & 0xff00) << 8) + ((i & 0xff0000) >> 8) + ((i & 0xff000000) >> 24));
        }
    }
}
