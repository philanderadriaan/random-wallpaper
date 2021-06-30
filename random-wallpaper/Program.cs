using System;

namespace random_wallpaper
{
    class Program
    {
        static string PropFileName = "config.properties";
        static string SrcKey = "src";
        static string DestKey = "dest";

        static void Main(string[] args)
        {
            log("Hello World!");
        }

        static void log(string msg)
        {
            Console.WriteLine(msg);
        }
    }
}
