package arktool.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;
 

import arktool.Activator;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
@SuppressWarnings("restriction")
public class EcReaderAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public EcReaderAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	static WorkbenchWindow workbenchWindow = (WorkbenchWindow) PlatformUI
			.getWorkbench().getActiveWorkbenchWindow();

	static {
		workbenchWindow.setStatusLineVisible(true);
	}
	static IActionBars bars = workbenchWindow.getActionBars();
	static IStatusLineManager lineManager = bars.getStatusLineManager();
	static StatusLineContributionItem statusItem = new StatusLineContributionItem(
			"ReadStatus", 100);
	static {
		statusItem.setText("");
		lineManager.add(statusItem);
		lineManager.update(true);
	}
	public static String filePath =Activator.getDefault().getPreferenceStore().getString("$TXTPATH_KEY"); 
	public static String encoding = "GBK";
	static File file = new File(filePath);
	public static void reload(){
		System.out.println("Reloading............");
		 file = new File(filePath);
		if (file.isFile() && file.exists()) {
			encoding  =Activator.getDefault().getPreferenceStore().getString("$ENCODE_KEY"); // 字符编码(可解决中文乱码问题 )
			System.out.println(encoding);
			try {
				read = new InputStreamReader(new FileInputStream(file),
						encoding);
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				 
				e.printStackTrace();
			}

		}
		bufferedReader = new BufferedReader(read);
	}
	
	static InputStreamReader read = null;
	static {

		System.out.println(encoding+"********");
		if (file.isFile() && file.exists()) {
			encoding  =Activator.getDefault().getPreferenceStore().getString("$ENCODE_KEY"); // 字符编码(可解决中文乱码问题 )
		
			try {
				read = new InputStreamReader(new FileInputStream(file),
						encoding);
			} catch (UnsupportedEncodingException e) {
	 
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				 
				e.printStackTrace();
			}

		}

	}
	static BufferedReader bufferedReader = new BufferedReader(read);
	static String lineTXT = null;
	static int nowLine = 0;
	static{
		try {
			if(Activator.getDefault().getPreferenceStore().getBoolean("$AUTO_SAVE_KEY")){
			skip();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	static void skip() throws IOException{
		String NOW_LINE=Activator.getDefault().getPreferenceStore().getString("$NOWLINE_KEY");
		if(NOW_LINE.equals("")||NOW_LINE==null){
			NOW_LINE="0";
		}
		bufferedReader.skip(Integer.valueOf(NOW_LINE));
	}
	public void run(IAction action) {
		
		try {
			
			if ((lineTXT = bufferedReader.readLine()) != null) {
				while(lineTXT.length()==0){
					lineTXT = bufferedReader.readLine();
				}
			
				nowLine+=lineTXT.length();
				Activator.getDefault().getPreferenceStore().setValue("$NOWLINE_KEY", nowLine);
				workbenchWindow.setStatusLineVisible(true);
				statusItem.setText(lineTXT.toString().trim());
				lineManager.update(true);
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
//		try {
//			read.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.setWindow(window);
	}
	public IWorkbenchWindow getWindow() {
		return window;
	}
	public void setWindow(IWorkbenchWindow window) {
		this.window = window;
	}
}