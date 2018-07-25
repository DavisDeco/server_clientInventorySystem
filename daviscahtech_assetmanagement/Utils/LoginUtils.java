/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author davis
 */
public class LoginUtils {
    
    private String n;
    private String z;

    public LoginUtils() {
        
        setN("admin");
        setZ("daviscahtech2017%assetcheck");        
    }

    /**
     * @return the n
     */
    public String getN() {
        return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(String n) {
        if (n.length() < 35) {
            this.n = DigestUtils.sha1Hex(n);
        } else {
            this.n = n;
        }
    }

    /**
     * @return the z
     */
    public String getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(String z) {
        if (z.length() < 35) {
            this.z = DigestUtils.sha1Hex(z);
        } else {
            this.z = z;
        }
    }
    
    
    
}// end of class
