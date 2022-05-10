package pers.fjl.encrypt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.fjl.encrypt.rsa.RsaKeys;
import pers.fjl.encrypt.service.RsaService;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EncryptTest {

    @Resource
    private RsaService rsaService;

    @Test
    public void testRsa(){
        //声明解密后的数据变量
        String decryptData = null;
        String s = "j/KLZD AW6sq2zjmF3HbVYEL LCzkhzLdN4UEIuJJZxmUjaWNQs65/f4GRpUp4PG9JqSUr9sKtbyfkZl1KbAh cRYQjfXPve8xMUzhCVxmFtx4qhOJssH LJoO3ywcpITLU9JCHXn2AtIFQ7MCpg112xeACn2gbWCmX5EdZ9GD fGaFxdGYdMre8FGUpWSUvnEsPbxh4/CYrf/v1SASExBXyQIA8jTM5M/jGeArv7KJxQFCD0n4poxGzQYFHIsZeZ2UBLnYNCH8cbBALtIY4xcUp6UFUZbwVoHoXmrsHljtz0ofb86lU6CgLOEXjMNeh3uVGi2leRbqQ WUti6TM0Q: ";
        String s2 = s.replace(' ', '+');
        try {
            decryptData = rsaService.RSADecryptDataPEM(s2, RsaKeys.getServerPrvKeyPkcs8());
            System.out.println(decryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
