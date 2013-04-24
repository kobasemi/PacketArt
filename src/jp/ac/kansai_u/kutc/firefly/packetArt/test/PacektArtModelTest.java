/**
 * 
 */
package jp.ac.kansai_u.kutc.firefly.packetArt.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.ac.kansai_u.kutc.firefly.packetArt.*;
import jp.ac.kansai_u.kutc.firefly.packetArt.playing.BlockType;
import jp.ac.kansai_u.kutc.firefly.packetArt.playing.PacketrisModel;

/**
 * パケリスの動きをテストするクラスです。
 * @author midolin
 *
 */
public class PacektArtModelTest {
	PacketrisModel model;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		model = new PacketrisModel();
		model.initialize();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * 盤面状況を取得できるかどうかを確認するテスト
	 */
	@Test
	public void getBoardTest() {
		assertNotNull(model.getBoard());
	}
	
	/**
	 * 0,0が壁かどうかを確かめるテスト
	 */
	@Test
	public void pickZeroZeroBlockTest(){
		assertTrue(model.getBlock(0, 0).getBlockType() == BlockType.Wall);
		assertTrue(model.getBlock(0, 0).getLocation().equals(new Location(0, 0)));
	}
	
	@Test
	/**
	 * ミノを回転させるテスト
	 */
	public void RotationTest(){
		fail("test is not implemented.");
	}
	
	/**
	 * 行を消すテスト
	 */
	@Test
	public void DeleteLineTest(){
		fail("test is not implemented.");
	}
	
	/**
	 * ゲームオーバーのテスト
	 */
	@Test
	public void GameOverTest(){
		fail("test is not implemented.");
	}
	
	/**
	 * ハードドロップのテスト
	 */
	@Test
	public void HardDropTest(){
		fail("test is not implemented.");
	}
}
