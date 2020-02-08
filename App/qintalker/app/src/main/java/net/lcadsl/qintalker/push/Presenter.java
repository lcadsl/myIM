package net.lcadsl.qintalker.push;


import android.text.TextUtils;

public class Presenter implements Ipresenter{

    private IView mView;



    public Presenter(IView view){
        mView = view;
    }

    @Override
    public void search() {
        //开启界面loading
        String inputString = mView.getInputString();
        if (TextUtils.isEmpty(inputString)){
            //为空直接返回
            return;
        }

        int hashCode = inputString.hashCode();

        IUserService service = new UserService();

        String serviceResult = service.search(hashCode);

        String result = "Result:" + inputString + "-" + serviceResult;

        //关闭界面loading

        mView.setResultString(result);
    }
}
