/**
 * 
 */
package asia.jeremie;

import java.util.Stack;
import java.util.Vector;

/**核心算法
 * @author Jeremie
 *
 * TODO 实现新的生成算法
 * TODO 实现自动插旗算法
 * TODO 实现翻转提示算法
 *
 */
public class Core {
	
	/**
	 * 地雷矩阵
	 * 第一个参数y
	 * 第二个参数x
	 */
	public Flag[][] data ;
	
	/**
	 * 蒙板矩阵
	 * 第一个参数y
	 * 第二个参数x
	 * true 翻开
	 * false 未翻开
	 */
	public boolean[][] mask;
	
	private int lx;
	private int ly;
	
	/**
	 * 总雷数
	 */
	 private int Bi;
	 
	 /**
	  * 剩余方块数量
	  */
	 private int Mi;
	 
	 /**
	  * 小旗数量
	  */
	 private int Fi;
	 
	 /**
	  * 小旗矩阵
	  * true插上	false去除
	  */
	 public boolean[][] sf;
	
	/**
	 * 已经初始化
	 */
	 private boolean areCreate = false;
	
	/**
	 * 
	 */
	public Core() {
	}
	
	/**
	 * 初始化
	 * @param lx 长
	 * @param ly 宽
	 * @param iB 雷数
	 * @return 创建成功
	 */
	public boolean initial( int lx, int ly, int iB) {
		this.lx = lx;
		this.ly = ly;
		this.Bi = iB;
		this.Mi = lx*ly;
		if ( lx * ly < iB ) {		// 超过雷数限制
			return false;
		}
		if ( lx < 2 || ly < 2 ) {		// 过小
			return false;
		}
		// 初始化数组
		data = new Flag[ly][lx];	
		mask = new boolean[ly][lx];
		sf = new boolean[ly][lx];
		for (int y = 0; y < data.length; y++) {		// y
			for (int x = 0; x < data[y].length; x++) {		// x
				data[y][x]= Flag.F0;
				mask[y][x] = false;
				sf[y][x] = false; 
			}
		}
		return true;
	}
	
	/**
	 * 创建地雷
	 * 参数为不可为地雷的坐标
	 * @param nx 点击坐标x
	 * @param ny 点击坐标y
	 * @return true
	 */
	public boolean Create( int nx, int ny) {
		
		// 生成雷  TODO 实现新的生成算法
		Vector<Vector2D> vd = new Vector<>();
		for (int y = 0; y < data.length; y++) {	// y
			for (int x = 0; x < data[y].length; x++) {		// x
				if ( x != nx || y != ny ) {		// 不是指定项
					vd.add( new Vector2D(x, y));
				}
			}
		}
		if (vd.size() < lx*ly-1) {
			System.err.println("vd.size() < lx*ly-1");
			System.err.println("vd.size():"+vd.size());
			return false;
		}
		if (vd.size() == 0) {
			System.err.println("vd.size() == 0");
			return false;
		}
		for (int i = 0; i < Bi; i++) {
			int a = random(vd.size()-1);
			data[vd.get(a).y][vd.get(a).x] = Flag.Boom;
			vd.remove(a);
		}
		
		// 计算数字
		for (int y = 0; y < data.length; y++) {		// y
			for (int x = 0; x < data[y].length; x++) {		// x
				if (data[y][x] != Flag.Boom) {
					int i = 0;
					
					if (x-1>=0) {
						i+=isBoom(x-1, y);	// 左
						if (y-1>=0) {
							i+=isBoom(x-1, y-1);	// 左上
						}
						if (y+1<ly) {
							i+=isBoom(x-1, y+1);	// 左下
						}
					}
					
					if (y-1>=0) {
						i+=isBoom(x, y-1);	// 上
					}
					if (y+1<ly) {
						i+=isBoom(x, y+1);	// 下
					}
					
					if (x+1<lx) {
						i+=isBoom(x+1, y);	// 右
						if (y-1>=0) {
							i+=isBoom(x+1, y-1);	// 右上
						}
						if (y+1<ly) {
							i+=isBoom(x+1, y+1);	// 右下
						}
					}
					
					setF(x, y, i);
				}
			}
		}
		
		
		areCreate = true;		// 已经初始化
		return true;
	}
	
	/**
	 * 点击
	 * @param x 点击坐标x
	 * @param y 点击坐标y
	 * @return 实际内容
	 */
	private Flag Hit( int x, int y) {
		if (!areCreate) {		// 未初始化
			Create(x, y);
//			Run.Run.sound.displaySoundBegin();
		}
		return data[y][x];
	}
	
	/**
	 * 随机数生成器
	 * @param max 随机数最大值
	 * @return	结果取值范围 0~max
	 */
	private int random( int max) {
		return (int) (Math.random() * (max+1));		// (max+1)	使得结果取值范围 0~max
	}
	
	/**
	 * isBoom
	 * @param x 坐标x
	 * @param y 坐标y
	 * @return true 1		false 0
	 */
	private int isBoom( int x, int y) {
		if (data[y][x] == Flag.Boom) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 设置标志
	 * @param x 坐标x
	 * @param y 坐标y
	 * @param n 标志n
	 */
	private void setF( int x, int y, int n) {
		switch (n) {
		case 0:
			data[y][x] = Flag.F0;
			break;

		case 1:
			data[y][x] = Flag.F1;
			break;

		case 2:
			data[y][x] = Flag.F2;
			break;

		case 3:
			data[y][x] = Flag.F3;
			break;

		case 4:
			data[y][x] = Flag.F4;
			break;

		case 5:
			data[y][x] = Flag.F5;
			break;

		case 6:
			data[y][x] = Flag.F6;
			break;

		case 7:
			data[y][x] = Flag.F7;
			break;

		case 8:
			data[y][x] = Flag.F8;
			break;
			
		default:
			System.err.println("setF default");
			break;
		}
	}
	
	/**
	 * 泛洪填充算法	flood fill 
	 * @param hx 宽
	 * @param hy 高
	 */
	public Flag FloodFill( int hx, int hy) {
		Stack<Vector2D> stack = new Stack<>();
		stack.push( new Vector2D(hx, hy));
		do {
			Vector2D v2d = stack.pop();
			int x = v2d.x;
			int y = v2d.y;
			if ( !mask[y][x] ) {
				if ( isFlag(x, y) ) {
					setFlag(x, y);
				}
				mask[y][x] = true;	// 没有翻开就翻开
				--Mi;
				if ( Flag.F0 == Hit(x, y) ) {	
					// 是空	周围入栈
					if (x-1 >= 0) {
						stack.push( new Vector2D(x-1, y));
						if (y-1>=0) {
							stack.push( new Vector2D(x-1, y-1));
						}
						if (y+1<ly) {
							stack.push( new Vector2D(x-1, y+1));
						}
					}
					if (x+1 < lx) {
						stack.push( new Vector2D(x+1, y));
						if (y-1>=0) {
							stack.push( new Vector2D(x+1, y-1));
						}
						if (y+1<ly) {
							stack.push( new Vector2D(x+1, y+1));
						}
					}
					if (y-1 >= 0) {
						stack.push( new Vector2D(x, y-1));
					}
					if (y+1 < ly) {
						stack.push( new Vector2D(x, y+1));
					}
				}
			}
		} while ( !stack.isEmpty() );
		return Hit(hx, hy);
	}

	/**总雷数
	 * @return bi
	 */
	public int getBi() {
		return Bi;
	}

	/**剩余方块数
	 * @return mi
	 */
	public int getMi() {
		return Mi;
	}

	/**
	 * 转换小旗
	 * @param x 坐标x
	 * @param y 坐标y
	 * @return		成功true		mask该位置为true翻开则false
	 */
	public boolean setFlag( int x, int y) {
		return setFlag(x, y,  ( !sf[y][x] ) );	// 翻转
	}
	
	/**
	 * 设置小旗
	 * @param x 坐标x
	 * @param y 坐标y
	 * @param f	true插上 false去除
	 * @return		成功true		mask该位置为true翻开则false
	 */
	public boolean setFlag( int x, int y, boolean f) {
		if ( mask[y][x] ) {
			return false;
		} else {
			sf[y][x] = f;
			if (f) {
				++Fi;
			} else {
				--Fi;
			}
//			System.out.println("Fi:" + Fi);
//
//			for (int ty = 0; ty < data.length; ty++) {		// y
//				for (int tx = 0; tx < data[ty].length; tx++) {		// x
//					System.out.print( sf[ty][tx]?"\t1":"\t" );
//				}
//				System.out.println();
//			}
			
			return true;
		}
	}
	
	/**
	 * 检测小旗
	 * @param x 坐标x
	 * @param y 坐标
	 * @return 是否有flag
	 */
	public boolean isFlag( int x, int y) {
		return sf[y][x];
	}

	/**获取小旗数量
	 * 总雷数 - 小旗数量 = 剩余雷数
	 * @return fi
	 */
	public int getFi() {
		return Fi;
	}
	
	
	
}
