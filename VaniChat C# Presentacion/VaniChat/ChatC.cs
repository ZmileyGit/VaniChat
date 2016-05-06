using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace VaniChat
{
    public class ChatC
    {
        public int id { get; set; }
        public string users { get; set; }

        public ChatC(int id, string users)
        {
            this.id = id;
            this.users = users;
        }

        public override string ToString()
        {
            return $"Users: {users}, Cid: {id}";
        }
    }
}
