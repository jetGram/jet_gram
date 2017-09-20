package finalsoft.Menu;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.finalsoft.messenger.R;

import java.util.List;

import finalsoft.Menu.interfaces.OnItemClickListener;
import finalsoft.Menu.interfaces.OnItemLongClickListener;
import finalsoft.Menu.interfaces.OnMenuItemClickListener;
import finalsoft.Menu.interfaces.OnMenuItemLongClickListener;


public class ContextMenuDialogFragment extends DialogFragment implements OnItemClickListener, OnItemLongClickListener {

    public static final String TAG = ContextMenuDialogFragment.class.getSimpleName();
    private static final String BUNDLE_MENU_PARAMS = "BUNDLE_MENU_PARAMS";

    private LinearLayout mWrapperButtons;
    private LinearLayout mWrapperText;
    private MenuAdapter mDropDownMenuAdapter;
    private OnMenuItemClickListener mItemClickListener;
    private OnMenuItemLongClickListener mItemLongClickListener;
    private MenuParams mMenuParams;

    @Deprecated
    public static ContextMenuDialogFragment newInstance(int actionBarSize, List<MenuObject> menuObjects) {
        MenuParams params = new MenuParams();
        params.setActionBarSize(actionBarSize);
        params.setMenuObjects(menuObjects);
        return newInstance(params);
    }

    @Deprecated
    public static ContextMenuDialogFragment newInstance(int actionBarSize, List<MenuObject> menuObjects, int animationDelay) {
        MenuParams params = new MenuParams();
        params.setActionBarSize(actionBarSize);
        params.setMenuObjects(menuObjects);
        params.setAnimationDelay(animationDelay);
        return newInstance(params);
    }

    @Deprecated
    public static ContextMenuDialogFragment newInstance(int actionBarSize, List<MenuObject> menuObjects, int animationDelay, int animationDuration) {
        MenuParams params = new MenuParams();
        params.setActionBarSize(actionBarSize);
        params.setMenuObjects(menuObjects);
        params.setAnimationDelay(animationDelay);
        params.setAnimationDuration(animationDuration);
        return newInstance(params);
    }

    @Deprecated
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static ContextMenuDialogFragment newInstance(int actionBarSize, List<MenuObject> menuObjects,
                                                        int animationDelay, int animationDuration,
                                                        boolean fitsSystemWindow, boolean clipToPadding) {
        MenuParams params = new MenuParams();
        params.setActionBarSize(actionBarSize);
        params.setMenuObjects(menuObjects);
        params.setAnimationDelay(animationDelay);
        params.setAnimationDuration(animationDuration);
        params.setFitsSystemWindow(fitsSystemWindow);
        params.setClipToPadding(clipToPadding);
        return newInstance(params);
    }

    public static ContextMenuDialogFragment newInstance(MenuParams menuParams) {
        ContextMenuDialogFragment fragment = new ContextMenuDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_MENU_PARAMS, menuParams);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.MenuFragmentStyle);
        if (getArguments() != null) {
            mMenuParams = getArguments().getParcelable(BUNDLE_MENU_PARAMS);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        rootView.setFitsSystemWindows(mMenuParams.isFitsSystemWindow());
        ((ViewGroup) rootView).setClipToPadding(mMenuParams.isClipToPadding());

        initViews(rootView);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        initDropDownMenuAdapter();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDropDownMenuAdapter.menuToggle();
            }
        }, mMenuParams.getAnimationDelay());

        if (mMenuParams.isClosableOutside()) {
            rootView.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAdded()) {
                        dismiss();
                    }
                }
            });
        }
        return rootView;
    }

    private void initViews(View view) {
        mWrapperButtons = (LinearLayout) view.findViewById(R.id.wrapper_buttons);
        mWrapperText = (LinearLayout) view.findViewById(R.id.wrapper_text);
    }

    private void initDropDownMenuAdapter() {
        mDropDownMenuAdapter = new MenuAdapter(getActivity(), mWrapperButtons, mWrapperText,
                mMenuParams.getMenuObjects(), mMenuParams.getActionBarSize());
        mDropDownMenuAdapter.setOnItemClickListener(this);
        mDropDownMenuAdapter.setOnItemLongClickListener(this);
        mDropDownMenuAdapter.setAnimationDuration(mMenuParams.getAnimationDuration());
    }

    private void close() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, mMenuParams.getAnimationDelay());
    }

    public void setItemLongClickListener(OnMenuItemLongClickListener itemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener;
    }

    public void setItemClickListener(OnMenuItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    /**
     * Menu item click method
     */
    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onMenuItemClick(v, ((ViewGroup) v.getParent()).indexOfChild(v));
        }
        close();
    }

    @Override
    public void onLongClick(View v) {
        if (mItemLongClickListener != null) {
            mItemLongClickListener.onMenuItemLongClick(v, ((ViewGroup) v.getParent()).indexOfChild(v));
        }
        close();
    }
}