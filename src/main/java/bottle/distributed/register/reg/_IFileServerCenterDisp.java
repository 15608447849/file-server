// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.6.3
//
// <auto-generated>
//
// Generated from file `file_service_reg.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package bottle.distributed.register.reg;

/**
 * 提供 动态注册于删除文件服务器信息接口
 * 提供客户端 获取有效文件服务器信息
 **/
public abstract class _IFileServerCenterDisp extends Ice.ObjectImpl implements IFileServerCenter
{
    protected void
    ice_copyStateFrom(Ice.Object __obj)
        throws java.lang.CloneNotSupportedException
    {
        throw new java.lang.CloneNotSupportedException();
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::reg::IFileServerCenter"
    };

    public boolean ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[] ice_ids()
    {
        return __ids;
    }

    public String[] ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    public String ice_id()
    {
        return __ids[1];
    }

    public String ice_id(Ice.Current __current)
    {
        return __ids[1];
    }

    public static String ice_staticId()
    {
        return __ids[1];
    }

    /**
     * 动态配置文件服务器地址信息
     **/
    public final boolean dynamicRegistrationFileServerInfo(FSAddressConfig[] list, boolean isCovered)
    {
        return dynamicRegistrationFileServerInfo(list, isCovered, null);
    }

    /**
     * 动态删除文件服务器地址信息
     **/
    public final boolean dynamicRemoveFileServerInfo(FSAddressConfig[] list)
    {
        return dynamicRemoveFileServerInfo(list, null);
    }

    /**
     * 查询有效文件服务器地址
     **/
    public final FSAddressInfo queryFileServerAddress()
    {
        return queryFileServerAddress(null);
    }

    public static Ice.DispatchStatus ___queryFileServerAddress(IFileServerCenter __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.readEmptyParams();
        FSAddressInfo __ret = __obj.queryFileServerAddress(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        FSAddressInfo.__write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___dynamicRegistrationFileServerInfo(IFileServerCenter __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        FSAddressConfig[] list;
        boolean isCovered;
        list = FSAddressConfigListHelper.read(__is);
        isCovered = __is.readBool();
        __inS.endReadParams();
        boolean __ret = __obj.dynamicRegistrationFileServerInfo(list, isCovered, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___dynamicRemoveFileServerInfo(IFileServerCenter __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        FSAddressConfig[] list;
        list = FSAddressConfigListHelper.read(__is);
        __inS.endReadParams();
        boolean __ret = __obj.dynamicRemoveFileServerInfo(list, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "dynamicRegistrationFileServerInfo",
        "dynamicRemoveFileServerInfo",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "queryFileServerAddress"
    };

    public Ice.DispatchStatus __dispatch(IceInternal.Incoming in, Ice.Current __current)
    {
        int pos = java.util.Arrays.binarySearch(__all, __current.operation);
        if(pos < 0)
        {
            throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return ___dynamicRegistrationFileServerInfo(this, in, __current);
            }
            case 1:
            {
                return ___dynamicRemoveFileServerInfo(this, in, __current);
            }
            case 2:
            {
                return ___ice_id(this, in, __current);
            }
            case 3:
            {
                return ___ice_ids(this, in, __current);
            }
            case 4:
            {
                return ___ice_isA(this, in, __current);
            }
            case 5:
            {
                return ___ice_ping(this, in, __current);
            }
            case 6:
            {
                return ___queryFileServerAddress(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    protected void __writeImpl(IceInternal.BasicStream __os)
    {
        __os.startWriteSlice(ice_staticId(), -1, true);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        __is.endReadSlice();
    }

    public static final long serialVersionUID = 0L;
}
