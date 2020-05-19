function printStack(name) {
    Java.perform(function () {
        var Exception = Java.use("java.lang.Exception");
        var ins = Exception.$new("Exception");
        var straces = ins.getStackTrace();
        if (straces != undefined && straces != null) {
            var strace = straces.toString();
            var replaceStr = strace.replace(/,/g, "\\n");
            console.log("=============================" + name + " Stack strat=======================");
            console.log(replaceStr);
            console.log("=============================" + name + " Stack end=======================\r\n");
            Exception.$dispose();
        }
    });
}

function main() {
    Java.perform(function () {
            var StringClass = Java.use("java.lang.String");
            var KeyStore = Java.use("java.security.KeyStore");
            KeyStore.load.overload('java.security.KeyStore$LoadStoreParameter').implementation = function (arg0) {
                printStack("KeyStore.load1");
                console.log("KeyStore.load1:", arg0);
                this.load(arg0);
            };
            KeyStore.load.overload('java.io.InputStream', '[C').implementation = function (arg0, arg1) {
                printStack("KeyStore.load2");
                console.log("KeyStore.load2:", arg0, arg1 ? StringClass.$new(arg1) : null);
                this.load(arg0, arg1);
            };
            KeyStore.load.overload()

            console.log("hook_KeyStore_load...");
        });
}


function binaryToHexToAscii(array, readLimit) {
    var result = [];
    // read 100 bytes #performance
    readLimit = readLimit || 100;
    for (var i = 0; i < readLimit; ++i) {
        result.push(String.fromCharCode( // hex2ascii part
            parseInt(
                ('0' + (array[i] & 0xFF).toString(16)).slice(-2), // binary2hex part
                16
            )
        ));
    }
    return result.join('');
}


function hook_platform(){
    Java.perform(function() {
        var ClassName = "java.security.cert.CertificateFactory";
        var certi = Java.use(ClassName);
        certi.getInstance.overload('java.lang.String').implementation = function (arg0) {
            console.log("generateCertificate:", arg0);

            this.generateCertificate.overload("java.io.InputStream").implementation = function(arg1){
                arg1.read.overload('[B').implementation = function(b) {
                    // execute original and save return value
                    var retval = this.read(b);
                    var resp = binaryToHexToAscii(b);
                    // conditions to not print garbage packets
                    var reExcludeList = new RegExp(['Mmm'/*, 'Ping' /*, ' Yo'*/].join('|'));
                    if ( ! reExcludeList.test(resp) ) {
                        console.log(resp);
                    }
                    var reIncludeList = new RegExp(['AAA', 'BBB', 'CCC'].join('|')); 
                    if ( reIncludeList.test(resp) ) {
                        send( binaryToHexToAscii(b, 1200) );
                    }
                    var ByteString = Java.use("com.android.okhttp.okio.ByteString");
                    console.log("ByteString:", ByteString.of(b).hex())
                    return retval;
                };
                return this.generateCertificate(arg1)
            };

            return this.getInstance(arg0);
        };

        // var trustManager= Java.use("com.neuifo.mangareptile.utils.ViewUtils")
        // trustManager.decodeBoldString.overload("java.lang.String").implementation = function (arg0) {
        //     console.log("decodeBoldString:", arg0);
        //     return this.decodeBoldString(arg0);
        // };

        var trustManager=Java.use("javax.net.ssl.TrustManagerFactory")
        trustManager.getInstance.overload('java.lang.String').implementation = function (arg0) {
            console.log("getInstance:", arg0);
            return this.getInstance(arg0);
        };
    });
}

function hook_assest(){
    Java.perform(function() {
        var ClassName = "android.content.res.AssetManager";
        var certi = Java.use(ClassName);
        certi.open.overload('java.lang.String').implementation= function (arg0) {
            console.log("file name:", arg0);
            return this.open(arg0);
        };


        var resoiure=Java.use("android.content.res.Resources");
        resoiure.openRawResource.overload('int').implementation =function(arg0){
            console.log("raw name:", arg0);
            return this.openRawResource(arg0)
        };

    });
}


function hook_dmzj(){
    Java.perform(function() {
        var ClassName = "com.nostra13.universalimageloader.core.ImageLoader";
        var ImageLoader = Java.use(ClassName);
        console.log("hook ks base adapter:", ImageLoader);
        ImageLoader.displayImage.overload('java.lang.String','android.widget.ImageView').implementation = function (arg0,arg1) {
            console.log("displayImage:", arg0);
            this.displayImage(arg0,arg1);
        };


        var ClassName2 = "com.dmzj.manhua.ui.adapter.KDBaseAdapter";
        var adapter2 = Java.use(ClassName2);
        console.log("hook ks base adapter:", adapter2);
        adapter2.showImages.overload('android.widget.ImageView','java.lang.String').implementation = function (arg0,arg1) {
            console.log("showImages:", arg1);
            this.showImages(arg0,arg1);
        };

        var ClassName3 = "com.dmzj.manhua.utils.ImgUtils";
        var imageUtils = Java.use(ClassName3);
        imageUtils.setImg.overload('android.widget.ImageView','java.lang.String').implementation = function (arg0,arg1) {
            console.log("setImg:", arg1);
            this.setImg(arg0,arg1);
        };
    });
}


function hook_ssl() {
    Java.perform(function() {
        var ClassName = "com.android.org.conscrypt.Platform";
        var Platform = Java.use(ClassName);
        var targetMethod = "checkServerTrusted";
        var len = Platform[targetMethod].overloads.length;
        console.log(len);
        for(var i = 0; i < len; ++i) {
            Platform[targetMethod].overloads[i].implementation = function () {
                console.log("第"+i+"个方法");
                console.log("class:", ClassName, "target:", targetMethod, " i:", i, arguments);
                printStack(ClassName + "." + targetMethod);
            }
        }
    });
}





function call_signature() {
    Java.perform(function () {
        // var currentApplication = Java.use("android.app.ActivityThread").currentApplication();
        // var context = currentApplication.getApplicationContext();
        var RpcSignUtil = Java.use("com.alipay.mobile.common.transport.utils.RpcSignUtil");

        var LauncherApplication = null;
        Java.choose("com.alipay.mobile.quinox.LauncherApplication", {
            onMatch: function (instance) {
                console.log("LauncherApplication instance:", instance);
                LauncherApplication = instance;
            }, onComplete: function () {
                if (LauncherApplication != null) {
                    var signdata = RpcSignUtil.signature(LauncherApplication, "9101430221728_ANDROID", false, "Operation-Type=alipay.client.updateVersion&Request-Data=W3siYXBrTWQ1IjoiYzAxOGUzMzZiYjNhYzIzODk0ODhiMDhhNzIwMjRlYzUiLCJjaGFubmVsIjoiYWxpcGF5IiwiY2xpZW50SWQiOiIxOTA3MTkwOTA5MzYwNTZ8MzUyNTMxMDgyNzQzODIyIiwiY2xpZW50UG9zdGlvbiI6IjExNi40NDA1NzIsMzkuOTE3OTY5IiwiZGlkIjoiWFNvVHloWWliS2dEQUhvMHJPU2FpVnN0IiwiZXh0SW5mb3MiOnsiY3B1SW5zdHJ1Y3Rpb25TZXRzIjoiW1wiYXJtZWFiaS12N2FcIixcImFybWVhYmlcIixcImFybTY0LXY4YVwiXSJ9LCJtb2JpbGVCcmFuZCI6IkFuZHJvaWQiLCJtb2JpbGVNb2RlbCI6IkFPU1Agb24gbXNtODk5NiIsIm5ldFR5cGUiOiI0RyIsIm9zVHlwZSI6ImFuZHJvaWQiLCJvc1ZlcnNpb24iOiI4LjEuMCIsInByaXNvbkJyZWFrIjpmYWxzZSwicHJvZHVjdElkIjoiOTEwMTQzMDIyMTcyOF9BTkRST0lELXByb2R1Y3QiLCJwcm9kdWN0VmVyc2lvbiI6IjQuMi4xMCJ9XQ==&Ts=Mm7uq/y", false, 0);
                    console.log("signdata", signdata.sign.value);
                    signdata.sign.value = "pediy";
                    console.log("signdata", signdata.sign.value);

                }
            }
        });

    });
}


function call_signature2() {
    Java.perform(function () {
        var currentApplication = Java.use("android.app.ActivityThread").currentApplication();
        var context = currentApplication.getApplicationContext();
        var RpcSignUtil = Java.use("com.alipay.mobile.common.transport.utils.RpcSignUtil");

        var signdata = RpcSignUtil.signature(context, "9101430221728_ANDROID", false, "pediy", false, 0);
        console.log("signdata", signdata.sign.value);
    });
}


function hook_dyn_dex() {
    Java.perform(function () {
        ///data/user/0/com.MobileTicket/app_SGLib/app_1563496271/main/libsgmain_312739200000.zip
        var str_find_class = "com.taobao.wireless.security.adapter.JNICLibrary";

        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass(str_find_class)) {
                        console.log("loader:", loader);
                        //主要是切换classFactory loader
                        Java.classFactory.loader = loader;
                    }
                } catch (error) {

                }

            }, onComplete: function () {

            }
        });

        var JNICLibrary = Java.use("com.taobao.wireless.security.adapter.JNICLibrary");
        console.log(JNICLibrary);
        //public static native Object doCommandNative(int i, Object... objArr);
        JNICLibrary.doCommandNative.implementation = function (i, objArr) {
            var result = this.doCommandNative(i, objArr);
            console.log("JNICLibrary.doCommandNative:", i, objArr, result);
            return result;
        };

    });
}

function hook_dex2() {
    Java.perform(function () {
        var str_find_class = "com.taobao.wireless.security.adapter.JNICLibrary";

        Java.choose("dalvik.system.DexClassLoader", {
            onMatch: function (entry) {
                console.log(entry);
                try {
                    if (entry.findClass(str_find_class)) {
                        console.log("find libsgmain DexClassLoader");
                        Java.classFactory.loader = entry;
                    }
                } catch (error) {
                }
            }, onComplete: function () {
            }
        })

        Java.choose("dalvik.system.PathClassLoader", {
            onMatch: function (entry) {
                console.log(entry);
                try {
                    if (entry.findClass(str_find_class)) {
                        console.log("find libsgmain PathClassLoader");
                        Java.classFactory.loader = entry;
                    }
                } catch (error) {
                }

            }, onComplete: function () {
            }
        });

        var JNICLibrary = Java.use(str_find_class);
        JNICLibrary.doCommandNative.implementation = function (i, objArr) {
            var result = this.doCommandNative(i, objArr);
            console.log("JNICLibrary.doCommandNative:", i, objArr, result);
            return result;
        };
        console.log("hook_dex2...");
    });
}

function hook_inner_class() {
    //hook 有名字的内部类
    var SignData = Java.use("com.alipay.mobile.common.transport.utils.RpcSignUtil$SignData");

    /*
    //hook 没有名字的内部类
    public void asyncNotifyRpcHeaderUpdateEvent(final Method method, final Object[] args, final HttpUrlResponse response) {
        NetworkAsyncTaskExecutor.execute(new Runnable() {
            public void run() {}
        }
    */
    var RpcInvoker_1 = Java.use("com.alipay.mobile.common.rpc.RpcInvoker$1");

}

//setTimeout(main, 0);
setImmediate(hook_dmzj);