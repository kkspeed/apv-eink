package cx.hell.android.lib.pagesview;


import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import android.view.View;
//import java.lang.reflect.InvocationTargetException;

/**
 * Nook Touch EPD controller interface wrapper.
 * This class is created by DairyKnight for Nook Touch screen support in FBReaderJ.
 * @author DairyKnight <dairyknight@gmail.com>
 * http://forum.xda-developers.com/showthread.php?t=1183173
 */

public class N2EpdController {
	public static final int REGION_APP_1 = 0;
	public static final int REGION_APP_2 = 1;
	public static final int REGION_APP_3 = 2;
	public static final int REGION_APP_4 = 3;
	
	public static final int WAVE_GC = 0;
	public static final int WAVE_GU = 1;
	public static final int WAVE_DU = 2;
	public static final int WAVE_A2 = 3;
	public static final int WAVE_GL16 = 4;
	public static final int WAVE_AUTO = 5;
	
	public static final int MODE_BLINK = 0;
	public static final int MODE_ACTIVE = 1;
	public static final int MODE_ONESHOT = 2;
	public static final int MODE_CLEAR = 3;
	public static final int MODE_ACTIVE_ALL = 4;
	public static final int MODE_ONESHOT_ALL = 5;
	public static final int MODE_CLEAR_ALL = 6;
	
	public static String strN2EpdInit = " N2EpdInit: ";
	
	private static Method mtSetRegion = null;
	private static Constructor RegionParamsConstructor= null;

	private static Object[] enumsWave 	= null;
	private static Object[] enumsRegion	= null;
	private static Object[] enumsMode	= null;

	static {
		try {
			Class clEpdController     	= Class.forName("android.hardware.EpdController");
			Class clEpdControllerWave 	= Class.forName("android.hardware.EpdController$Wave");
			Class clEpdControllerMode 	= Class.forName("android.hardware.EpdController$Mode");
			Class clEpdControllerRegion = Class.forName("android.hardware.EpdController$Region");

			Class clEpdControllerRegionParams = Class.forName("android.hardware.EpdController$RegionParams");
			
			enumsWave = clEpdControllerWave.getEnumConstants();

			enumsMode = clEpdControllerMode.getEnumConstants();

			enumsRegion = clEpdControllerRegion.getEnumConstants();

//				mtSetRegion = clEpdController.getMethod("setRegion", String.class, clEpdControllerRegion, View.class,
//								clEpdControllerWave, clEpdControllerMode);
			RegionParamsConstructor = clEpdControllerRegionParams.getConstructor(
					new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, clEpdControllerWave});
			mtSetRegion = clEpdController.getMethod("setRegion", String.class, clEpdControllerRegion, 
					clEpdControllerRegionParams, clEpdControllerMode);
			
			strN2EpdInit += "Ok!";
		} catch (Exception e) {
			System.err.println("Failed to init refresh EPD");
			System.err.println(e.toString());
			strN2EpdInit += "Failed: " + e.toString();
			e.printStackTrace();
		}
	}

//	public static void setMode(int region, int wave, int mode, View view) {
	public static void setMode(int region, int wave, int mode) {
		if (mtSetRegion != null) {
			try {
				Object regionParams =  RegionParamsConstructor.newInstance(new Object[] { 0, 0, 600, 800, enumsWave[wave]});
//				mtSetRegion.invoke(null, "CoolReader", enumsRegion[region], view, enumsWave[wave], enumsMode[mode]);
				mtSetRegion.invoke(null, "CoolReader", enumsRegion[region], regionParams, enumsMode[mode]);
			} catch (Exception e) {
				System.err.println("Failed: SetMode");
				System.err.println(e.toString());
				strN2EpdInit += "Failed: setMode: " + e.toString();
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void enterA2Mode() {
		System.err.println("APV::enterA2Mode");
		try {
			Class epdControllerClass = Class
					.forName("android.hardware.EpdController");
			Class epdControllerRegionClass = Class
					.forName("android.hardware.EpdController$Region");
			Class epdControllerRegionParamsClass = Class
					.forName("android.hardware.EpdController$RegionParams");
			Class epdControllerWaveClass = Class
					.forName("android.hardware.EpdController$Wave");

			Object[] waveEnums = null;
			if (epdControllerWaveClass.isEnum()) {
				System.err
						.println("EpdController Wave Enum successfully retrived.");
				waveEnums = epdControllerWaveClass.getEnumConstants();
			}

			Object[] regionEnums = null;
			if (epdControllerRegionClass.isEnum()) {
				System.err
						.println("EpdController Region Enum successfully retrived.");
				regionEnums = epdControllerRegionClass.getEnumConstants();
			}

			Constructor RegionParamsConstructor = epdControllerRegionParamsClass
					.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE,
							Integer.TYPE, Integer.TYPE, epdControllerWaveClass,
							Integer.TYPE });

			Object localRegionParams = RegionParamsConstructor
					.newInstance(new Object[] { 0, 0, 600, 800, waveEnums[2],
							16 }); // Wave = DU

			Method epdControllerSetRegionMethod = epdControllerClass.getMethod(
					"setRegion", new Class[] { String.class,
							epdControllerRegionClass,
							epdControllerRegionParamsClass });
			epdControllerSetRegionMethod
					.invoke(null, new Object[] { "APV-ReadingView",
							regionEnums[2], localRegionParams });

			Thread.sleep(100L);
			localRegionParams = RegionParamsConstructor
					.newInstance(new Object[] { 0, 0, 600, 800, waveEnums[3],
							14 }); // Wave = A2
			epdControllerSetRegionMethod
					.invoke(null, new Object[] { "APV-ReadingView",
							regionEnums[2], localRegionParams });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setGL16Mode() {
		System.err.println("APV::setGL16Mode");
		try {
			/*
			 * Loading the Epson EPD Controller Classes
			 */
			Class epdControllerClass = Class
					.forName("android.hardware.EpdController");
			Class epdControllerRegionClass = Class
					.forName("android.hardware.EpdController$Region");
			Class epdControllerRegionParamsClass = Class
					.forName("android.hardware.EpdController$RegionParams");
			Class epdControllerWaveClass = Class
					.forName("android.hardware.EpdController$Wave");
			Class epdControllerModeClass = Class
					.forName("android.hardware.EpdController$Mode");

			/*
			 * Creating EPD enums
			 */
			Object[] waveEnums = null;
			if (epdControllerWaveClass.isEnum()) {
				System.err
						.println("EpdController Wave Enum successfully retrived.");
				waveEnums = epdControllerWaveClass.getEnumConstants();
			}

			Object[] regionEnums = null;
			if (epdControllerRegionClass.isEnum()) {
				System.err
						.println("EpdController Region Enum successfully retrived.");
				regionEnums = epdControllerRegionClass.getEnumConstants();
			}

			Object[] modeEnums = null;
			if (epdControllerModeClass.isEnum()) {
				System.err
						.println("EpdController Region Enum successfully retrived.");
				modeEnums = epdControllerModeClass.getEnumConstants();
				System.err.println(modeEnums);
			}

			Constructor RegionParamsConstructor = epdControllerRegionParamsClass
					.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE,
							Integer.TYPE, Integer.TYPE, epdControllerWaveClass });

			Object localRegionParams = RegionParamsConstructor
					.newInstance(new Object[] { 0, 0, 600, 800, waveEnums[1] }); // Wave
																					// =
																					// GU

			Method epdControllerSetRegionMethod = epdControllerClass.getMethod(
					"setRegion", new Class[] { String.class,
							epdControllerRegionClass,
							epdControllerRegionParamsClass,
							epdControllerModeClass });
			epdControllerSetRegionMethod.invoke(null, new Object[] {
					"APV-ReadingView", regionEnums[2], localRegionParams,
					modeEnums[2] }); // Mode = ONESHOT_ALL
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
