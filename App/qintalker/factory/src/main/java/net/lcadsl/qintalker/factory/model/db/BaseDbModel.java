package net.lcadsl.qintalker.factory.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;

import net.lcadsl.qintalker.factory.utils.DiffUiDataCallback;

/**
 * app中的基础的BaseDbModel，继承DbFlow中的基础类 同时定义了需要的方法
 */
public abstract class BaseDbModel<Model> extends BaseModel
        implements DiffUiDataCallback.UiDataDiffer<Model> {
}
