package arktool.preferences;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import arktool.Activator;
import arktool.actions.EcReaderAction;

public class EcReaderPreferencePage extends RootPreferencePage implements
		IWorkbenchPreferencePage, ModifyListener {

	public static final String ENCODE_KEY = "$ENCODE_KEY";
	public static final String TXTPATH_KEY = "$TXTPATH_KEY";
	public static final String AUTO_SAVE_KEY = "$AUTO_SAVE_KEY";

	public static final String ENCODE_DEFAULT = "GBK";
	public static final String TXTPATH_DEFAULT = "C:/Novel.txt";
	// 定义

	private Text pathText;
	private Button AutoSave;
	private FileDialog filePathDialog;
	private Combo encoder;

	private IPreferenceStore ps;
	private Button pathButton;
	Shell dialogShell = new Shell();

	@Override
	public void init(IWorkbench arg0) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite topComp = new Composite(parent, SWT.NONE);
		topComp.setLayout(new GridLayout(3, false));

		new Label(topComp, SWT.NONE).setText("编码方式:");
		new Label(topComp, SWT.NONE).setText("");
		encoder = new Combo(topComp, SWT.DROP_DOWN | SWT.READ_ONLY);

		encoder.add("GBK");
		encoder.select(0);
		encoder.add("UTF-8");
		new Label(topComp, SWT.NONE).setText("Path:");
		pathText = new Text(topComp, SWT.BORDER);
		pathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pathButton = new Button(topComp, SWT.NONE);
		pathButton.setText("Browse");
		pathButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(topComp, SWT.NONE).setText("自动保存:");
		AutoSave = new Button(topComp, SWT.CHECK);
		AutoSave.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (Activator.getDefault().getPreferenceStore()
				.getBoolean("$PASSWORD_KEY")) {
			AutoSave.setSelection(true);
		} else {
			AutoSave.setSelection(false);
		}
		new Label(topComp, SWT.NONE).setText("");

		// 取得一个IPreferenceStore对象
		ps = getPreferenceStore();

		String filePath = ps.getString(TXTPATH_KEY);
		if (filePath == null || filePath.trim().equals(""))
			pathText.setText(TXTPATH_DEFAULT);
		else
			pathText.setText(filePath);

		encoder.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {

			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				int index = encoder.getSelectionIndex();
				String data = encoder.getItem(index);
				ps.setValue("$ENCODE_KEY", data);
			}

			@Override
			public void mouseUp(MouseEvent arg0) {

			}

		});
		pathButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {

			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				filePathDialog = new FileDialog(dialogShell, SWT.OPEN);
				filePathDialog.setFilterExtensions(new String[] { "*.txt" });
				String returnValue = filePathDialog.open();
				pathText.setText(returnValue);
				System.out.println(returnValue);

			}

			@Override
			public void mouseUp(MouseEvent arg0) {

			}

		});
		return topComp;
	}

	@Override
	public void modifyText(ModifyEvent arg0) {
		String errorStr = null;
		if (pathText.getText().trim().length() == 0) {
			errorStr = "路径不能为空！";
		}
		setErrorMessage(errorStr);
		setValid(errorStr == null);
		getApplyButton().setEnabled(errorStr == null);
	}

	protected void performDefaults() {
		encoder.setText(ENCODE_DEFAULT);
		pathText.setText(TXTPATH_DEFAULT);
		AutoSave.setSelection(true);
	}

	protected void performApply() {
		doSave();
		MessageDialog.openInformation(getShell(), "Info Ark",
				"Save Modified Successfully!");
	}

	public boolean performOk() {
		doSave();
		MessageDialog.openInformation(getShell(), "Info Ark",
				"Modified Configs Will Effects ...");
		return true;
	}

	private void doSave() {

		ps.setValue(TXTPATH_KEY, pathText.getText());
		ps.setValue(AUTO_SAVE_KEY, AutoSave.getSelection() ? true : false);
		EcReaderAction.encoding = encoder.getText();
		EcReaderAction.filePath = pathText.getText();

		EcReaderAction.reload();
	}

}
