package pers.fjl.encrypt.service;


import org.springframework.stereotype.Service;
import pers.fjl.encrypt.rsa.Base64Utils;
import pers.fjl.encrypt.rsa.RSA;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;


/**
 * RSA 加解密
 *
 * @author Administrator
 */
@Service("RsaService")
public class RsaServiceImpl implements RsaService {

    /***
     * RSA解密
     *
     * @param encryptData
     * @return
     * @throws Exception
     */
    public String RSADecryptDataPEM(String encryptData, String prvKey) throws Exception {
    	byte[] encryptBytes = encryptData.getBytes();
        byte[] prvdata = RSA.decryptByPrivateKey(Base64Utils.decode(encryptData), prvKey);

        String outString = new String(prvdata, "UTF-8");
        return outString;
    }

    @Override
	public String RSADecryptDataBytes(byte[] encryptBytes, String prvKey)
			throws Exception {
		// TODO Auto-generated method stub
    	byte[] prvdata = RSA.decryptByPrivateKey(encryptBytes, prvKey);
        String outString = new String(prvdata, "utf-8");
        return outString;
	}

    /***
     * RSA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public String RSAEncryptDataPEM(String data, String pubKey) throws Exception {

        byte[] pubdata = RSA.encryptByPublicKey(data.getBytes("UTF-8"), pubKey);
        String outString = new String(Base64Utils.encode(pubdata));

        return outString;
    }

	@Override
	public String getRsaAlgorithm() {
		// TODO Auto-generated method stub
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keyFactory.getAlgorithm();
	}


}
