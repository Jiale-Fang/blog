package pers.fjl.encrypt.rsa;

/**
 * rsa加解密用的公钥和私钥
 * @author Administrator
 *
 */
public class RsaKeys {

	//生成秘钥对的方法可以参考这篇帖子
	//https://www.cnblogs.com/yucy/p/8962823.html

//	//服务器公钥
//	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Dw9nwjBmDD/Ca1QnRGy"
//											 + "GjtLbF4CX2EGGS7iqwPToV2UUtTDDemq69P8E+WJ4n5W7Iln3pgK+32y19B4oT5q"
//											 + "iUwXbbEaAXPPZFmT5svPH6XxiQgsiaeZtwQjY61qDga6UH2mYGp0GbrP3i9TjPNt"
//											 + "IeSwUSaH2YZfwNgFWqj+y/0jjl8DUsN2tIFVSNpNTZNQ/VX4Dln0Z5DBPK1mSskd"
//											 + "N6uPUj9Ga/IKnwUIv+wL1VWjLNlUjcEHsUE+YE2FN03VnWDJ/VHs7UdHha4d/nUH"
//											 + "rZrJsKkauqnwJsYbijQU+a0HubwXB7BYMlKovikwNpdMS3+lBzjS5KIu6mRv1GoE"
//											 + "vQIDAQAB";
//
//	//服务器私钥(经过pkcs8格式处理)
//	private static final String serverPrvKeyPkcs8 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDoPD2fCMGYMP8J"
//				 								 + "rVCdEbIaO0tsXgJfYQYZLuKrA9OhXZRS1MMN6arr0/wT5YniflbsiWfemAr7fbLX"
//				 								 + "0HihPmqJTBdtsRoBc89kWZPmy88fpfGJCCyJp5m3BCNjrWoOBrpQfaZganQZus/e"
//				 								 + "L1OM820h5LBRJofZhl/A2AVaqP7L/SOOXwNSw3a0gVVI2k1Nk1D9VfgOWfRnkME8"
//				 								 + "rWZKyR03q49SP0Zr8gqfBQi/7AvVVaMs2VSNwQexQT5gTYU3TdWdYMn9UeztR0eF"
//				 								 + "rh3+dQetmsmwqRq6qfAmxhuKNBT5rQe5vBcHsFgyUqi+KTA2l0xLf6UHONLkoi7q"
//				 								 + "ZG/UagS9AgMBAAECggEBANP72QvIBF8Vqld8+q7FLlu/cDN1BJlniReHsqQEFDOh"
//				 								 + "pfiN+ZZDix9FGz5WMiyqwlGbg1KuWqgBrzRMOTCGNt0oteIM3P4iZlblZZoww9nR"
//				 								 + "sc4xxeXJNQjYIC2mZ75x6bP7Xdl4ko3B9miLrqpksWNUypTopOysOc9f4FNHG326"
//				 								 + "0EMazVaXRCAIapTlcUpcwuRB1HT4N6iKL5Mzk3bzafLxfxbGCgTYiRQNeRyhXOnD"
//				 								 + "eJox64b5QkFjKn2G66B5RFZIQ+V+rOGsQElAMbW95jl0VoxUs6p5aNEe6jTgRzAT"
//				 								 + "kqM2v8As0GWi6yogQlsnR0WBn1ztggXTghQs2iDZ0YkCgYEA/LzC5Q8T15K2bM/N"
//				 								 + "K3ghIDBclB++Lw/xK1eONTXN+pBBqVQETtF3wxy6PiLV6PpJT/JIP27Q9VbtM9UF"
//				 								 + "3lepW6Z03VLqEVZo0fdVVyp8oHqv3I8Vo4JFPBDVxFiezygca/drtGMoce0wLWqu"
//				 								 + "bXlUmQlj+PTbXJMz4VTXuPl1cesCgYEA6zu5k1DsfPolcr3y7K9XpzkwBrT/L7LE"
//				 								 + "EiUGYIvgAkiIta2NDO/BIPdsq6OfkMdycAwkWFiGrJ7/VgU+hffIZwjZesr4HQuC"
//				 								 + "0APsqtUrk2yx+f33ZbrS39gcm/STDkVepeo1dsk2DMp7iCaxttYtMuqz3BNEwfRS"
//				 								 + "kIyKujP5kfcCgYEA1N2vUPm3/pNFLrR+26PcUp4o+2EY785/k7+0uMBOckFZ7GIl"
//				 								 + "FrV6J01k17zDaeyUHs+zZinRuTGzqzo6LSCsNdMnDtos5tleg6nLqRTRzuBGin/A"
//				 								 + "++xWn9aWFT+G0ne4KH9FqbLyd7IMJ9R4gR/1zseH+kFRGNGqmpi48MS61G0CgYBc"
//				 								 + "PBniwotH4cmHOSWkWohTAGBtcNDSghTRTIU4m//kxU4ddoRk+ylN5NZOYqTxXtLn"
//				 								 + "Tkt9/JAp5VoW/41peCOzCsxDkoxAzz+mkrNctKMWdjs+268Cy4Nd09475GU45khb"
//				 								 + "Y/88qV6xGz/evdVW7JniahbGByQhrMwm84R9yF1mNwKBgCIJZOFp9xV2997IY83S"
//				 								 + "habB/YSFbfZyojV+VFBRl4uc6OCpXdtSYzmsaRcMjN6Ikn7Mb9zgRHR8mPmtbVfj"
//				 								 + "B8W6V1H2KOPfn/LAM7Z0qw0MW4jimBhfhn4HY30AQ6GeImb2OqOuh3RBbeuuD+7m"
//				 								 + "LpFZC9zGggf9RK3PfqKeq30q";

	//服务器公钥
	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzSJK+Pc1IdFWz83FWvKH" +
			"kTLmHq87J+5ndEdlH86c9AcSlaj4hyTWyaQOP8WPuvDfxL93TbYUEQ8bidHwyVAI" +
			"THoDABDQqBr0mDTfTqogHxWaqjEUh+g+y96KfshmD312lZD3cMxSrgA1NUlTBMpT" +
			"JQ+GQ1Rn0qVaVAi6OziAct9HEeFupJiw09sLzQTt5zY5s/KOgjQ7wPck4pGxO3tl" +
			"p/Iwws7WyabOjPm1z7XxcHWsetO9H6nKeJ8WaZ6P6baFasiNuhiBOvnGslRJ0Dgd" +
			"xsz7+feK3JbIwHr6vVfhJ9Bn2rnSYqpydg3odMyU54/lV0kdWup7SbJILAtaS8kv" +
			"EwIDAQAB";

	//服务器私钥(经过pkcs8格式处理)
	private static final String serverPrvKeyPkcs8 = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDNIkr49zUh0VbP" +
			"zcVa8oeRMuYerzsn7md0R2Ufzpz0BxKVqPiHJNbJpA4/xY+68N/Ev3dNthQRDxuJ" +
			"0fDJUAhMegMAENCoGvSYNN9OqiAfFZqqMRSH6D7L3op+yGYPfXaVkPdwzFKuADU1" +
			"SVMEylMlD4ZDVGfSpVpUCLo7OIBy30cR4W6kmLDT2wvNBO3nNjmz8o6CNDvA9yTi" +
			"kbE7e2Wn8jDCztbJps6M+bXPtfFwdax6070fqcp4nxZpno/ptoVqyI26GIE6+cay" +
			"VEnQOB3GzPv594rclsjAevq9V+En0GfaudJiqnJ2Deh0zJTnj+VXSR1a6ntJskgs" +
			"C1pLyS8TAgMBAAECggEATsFQuV7ndjFRu/xLPcyJQbSh5rvt4TnFXD4g0+JWHdYt" +
			"S2oQ6Im7MLUch11I/kOGFZpQqnQyJg5/yxzf7dodJYdeaYMLKM8YgushpKjqJiT8" +
			"OUUYlckTet/Ymi5ECMRpZ6i9Zv/66jTIOMoK5nbrDvpz2JuqNJQpJsQnA5+AqRjh" +
			"r3gq1wcwME3zFiC7BCtsSxKO6CHISHTov4LxgGOsmfinHP5lHoZxKnmal23OXahP" +
			"oWAuojVJ4y24hJTm1YAst32Zo4Hh4ivRebYGdfyjoOU+9HR957iF4W+bGb2zs4s5" +
			"FeY+pen2z+U4XXted4T+eG6dESJz2uuoG98oL58KqQKBgQDuMvIhQojSvpxSa37w" +
			"IwobDNS1tUFyccJitC44ys71H8EiFYHMuRZTdEeuV8gAWmGN7KrFn0EIQnXwWk1J" +
			"qwgKDbRdbxToxSTnKnFcMvwO0YmmAubpgf9ppctzirlNcLhDdPYUCAnMjyuN6W2N" +
			"pygyJTKMKlluGeocQ7mqaNpAnwKBgQDcdsUdodsMsop/6ishuVJmzh3rFlcNNh/B" +
			"nglGCuWCIJUU7XyNj3mfN3m+SHtZRKaEWA3XcBcTtFFsHkJvjy6TiqMOrOrwVjEj" +
			"j+i0nd1Jfx5IKU/pcBiFKtkTQYwi6IXOc9Ui6CC/XB/V3LLQzyDA4tvQjHpAKC/B" +
			"71jRm1+5DQKBgQDV5QWsfDSlGekB0emg9bYDaFgx+1uTyzeErsu6z9NcJnGli66N" +
			"Cb9UnVwo4EaGmqJzcYw//avGIPgLJuu0NVL4xCmspS0fgLiMpH47DsVtARgb7Qsx" +
			"sDyMwAab8HxJX+j3GjZG8pjqCb5QpsZrpyjfLqvfVcAMsFSboO0+av1hfQKBgQCi" +
			"OGacgj9rXWiZ7NWl7/ZZHStYk0yktQGy9zV9q4DrOkxYZNM0WrE0XZ6gTDcvHVul" +
			"oCE3OAxS/Gi5NJ7P9bxg5i9LGiOZiuKHd1nUpSBx9y5yDKv3afsw6bFnAOE7wnrK" +
			"yeK317RY+lGWjNmq2e4Q4By8nNFLqgZSHmrtePV7dQKBgQCq06PA6c8mdIz5tB1w" +
			"tmpBJlpGC7lggfjDT+Lf362ZC3lu/iyIcGp4qNpKzJvUGhFGh1NQ1QniVoY9boC9" +
			"MK3XoxbiyA8D+j2fk8bFX+ZKhMypm0JLJshAW6wcPay0/8qbVzFRKcIqCU2e1+Qd" +
			"2doDo3HCqW68aiANvUOhfhTRCg==";

	public static String getServerPubKey() {
		return serverPubKey;
	}

	public static String getServerPrvKeyPkcs8() {
		return serverPrvKeyPkcs8;
	}
	
}
