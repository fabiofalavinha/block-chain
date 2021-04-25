package test.com.faf.block.chain;

import com.faf.block.chain.Block;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BlockUnitTest {

    private static final List<Block> blockchain = new ArrayList<>();
    private static final int prefix = 4;
    private static final String prefixString = new String(new char[prefix]).replace('\0', '0');

    @BeforeClass
    public static void setUp() {
        final Block genesisBlock =
            new Block("The is the Genesis Block.", "0", new Date().getTime());
        genesisBlock.mineBlock(prefix);
        blockchain.add(genesisBlock);

        final Block firstBlock =
            new Block("The is the First Block.", genesisBlock.getHash(), new Date().getTime());
        firstBlock.mineBlock(prefix);
        blockchain.add(firstBlock);
    }

    @Test
    public void testAddNewBlock() {
        final Block newBlock = new Block(
            String.format("New block [%s]", new Date()),
            blockchain.get(blockchain.size() - 1).getHash(),
            new Date().getTime());

        newBlock.mineBlock(prefix);

        assertEquals(newBlock.getHash().substring(0, prefix), prefixString);

        blockchain.add(newBlock);
    }

    @Test
    public void testBlockchainVerification() {
        boolean verificationSuccess = true;

        for (int i = 1; i < blockchain.size() && verificationSuccess; i++) {
            final Block currentBlock = blockchain.get(i);
            final Block previousBlock = blockchain.get(i - 1);

            verificationSuccess =
                currentBlock.getHash().equals(currentBlock.calculateBlockHash()) &&
                previousBlock.getHash().equals(currentBlock.getPreviousHash()) &&
                currentBlock.getHash().substring(0, prefix).equals(prefixString);
        }

        assertTrue(verificationSuccess);
    }

    @AfterClass
    public static void tearDown() {
        for (Block block : blockchain) {
            System.out.println(block);
        }
        blockchain.clear();
    }
}
