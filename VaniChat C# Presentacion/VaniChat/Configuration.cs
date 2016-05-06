using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace VaniChat
{
    public static class Configuration
    {
        public static readonly Encoding DEFAULT_CHARSET = Encoding.BigEndianUnicode;
        public static readonly string SERVER_IP_ADDRESS = "127.0.0.1";
        public static readonly int DEFAULT_PORT = 8080;
        public static readonly byte LOGIN = 0;
        public static readonly byte GROUP = 1;
        public static readonly byte MESSAGE = 2;
        public static readonly byte USERLINK = 3;
        public static readonly byte CHATLINK = 4;
        public static readonly byte RECOVERCONV = 5;
        public static readonly string USERNAME_SEPARATOR = ":";
    }
}
