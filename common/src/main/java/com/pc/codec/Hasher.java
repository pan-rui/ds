/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pc.codec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import org.apache.shiro.crypto.UnknownAlgorithmException;

/**
 *
 * @author [*田园间*]   liaoxuqian@hotmail.com
 * @version 1.0
 * @datetime 2015-1-4  10:47:49
 */
public class Hasher {

    /***********************************************
    |             C O N S T A N T S             |
    ***********************************************/

    /***********************************************
    |    I N S T A N C E   V A R I A B L E S    |
    ***********************************************/
    private final String algorithmName;

    /***********************************************
    |         C O N S T R U C T O R S           |
     ***********************************************/
    public Hasher(String algorithmName) {
        super();
        this.algorithmName = algorithmName;
    }

    /***********************************************
    |  A C C E S S O R S / M O D I F I E R S    |
     ***********************************************/

    /***********************************************
    |               M E T H O D S               |
    ***********************************************/
    /**
     * Returns the JDK MessageDigest instance to use for executing the hash.
     *
     * @param algorithmName the algorithm to use for the hash, provided by subclasses.
     * @return the MessageDigest object for the specified {@code algorithm}.
     * @throws  if the specified algorithm name is not available.
     */
    protected MessageDigest getDigest(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getAlgorithmName() {
		return algorithmName;
	}

    /**
     * Hashes the specified byte array without a salt for a single iteration.
     *
     * @param bytes the bytes to hash.
     * @return the hashed bytes.
     */
    public byte[] hash(byte[] bytes) {
        return hash(bytes, null, 1);
    }

    /**
     * Hashes the specified byte array using the given {@code salt} for a single iteration.
     *
     * @param bytes the bytes to hash
     * @param salt  the salt to use for the initial hash
     * @return the hashed bytes
     */
    public byte[] hash(byte[] bytes, byte[] salt) {
        return hash(bytes, salt, 1);
    }

    /**
     * Hashes the specified byte array using the given {@code salt} for the specified number of iterations.
     *
     * @param bytes          the bytes to hash
     * @param salt           the salt to use for the initial hash
     * @param hashIterations the number of times the the {@code bytes} will be hashed (for attack resiliency).
     * @return the hashed bytes.
     * @throws  if the {@link #getAlgorithmName() algorithmName} is not available.
     */
    public byte[] hash(byte[] bytes, byte[] salt, int hashIterations){
        MessageDigest digest = getDigest(getAlgorithmName());
        if (salt != null) {
            digest.reset();
            digest.update(salt);
        }
        byte[] hashed = digest.digest(bytes);
        int iterations = hashIterations - 1; //already hashed once above
        //iterate remaining number:
        for (int i = 0; i < iterations; i++) {
            digest.reset();
            hashed = digest.digest(hashed);
        }
        return hashed;
    }
}
