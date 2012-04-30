
for(var i = 0; i < 41; i++) { var scriptId = 'u' + i; window[scriptId] = document.getElementById(scriptId); }

$axure.eventManager.pageLoad(
function (e) {

if (true) {

SetWidgetSelected('u4');
}

});
gv_vAlignTable['u31'] = 'top';gv_vAlignTable['u16'] = 'center';document.getElementById('u4_img').tabIndex = 0;
HookHover('u4', false);

u4.style.cursor = 'pointer';
$axure.eventManager.click('u4', function(e) {

if (true) {

    self.location.href="resources/reload.html#" + encodeURI($axure.globalVariableProvider.getLinkUrl($axure.pageData.url));

SetWidgetSelected('u4');
}
});
gv_vAlignTable['u20'] = 'top';document.getElementById('u29_img').tabIndex = 0;

u29.style.cursor = 'pointer';
$axure.eventManager.click('u29', function(e) {

if (true) {

	SetPanelState('u12', 'pd0u12','fade','',500,'fade','',500);

}
});
document.getElementById('u8_img').tabIndex = 0;
HookHover('u8', false);

u8.style.cursor = 'pointer';
$axure.eventManager.click('u8', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('chahuajiang.html');

SetWidgetSelected('u8');
}
});
gv_vAlignTable['u30'] = 'center';document.getElementById('u6_img').tabIndex = 0;
HookHover('u6', false);

u6.style.cursor = 'pointer';
$axure.eventManager.click('u6', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('rencaiku.html');

SetWidgetSelected('u6');
}
});
$('#u32').attr('axSubmit', 'u35');

$axure.eventManager.focus('u32', function(e) {

if ((GetWidgetText('u32')) == ('在此输入用户名')) {

SetWidgetFormText('u32', '');

}
});

$axure.eventManager.blur('u32', function(e) {

if ((GetWidgetText('u32')) == ('')) {

SetWidgetFormText('u32', '在此输入用户名');

}
});
gv_vAlignTable['u13'] = 'top';gv_vAlignTable['u14'] = 'top';gv_vAlignTable['u1'] = 'center';gv_vAlignTable['u26'] = 'top';document.getElementById('u10_img').tabIndex = 0;
HookHover('u10', false);

u10.style.cursor = 'pointer';
$axure.eventManager.click('u10', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Home.html');

}
});
gv_vAlignTable['u11'] = 'center';gv_vAlignTable['u3'] = 'center';gv_vAlignTable['u39'] = 'center';gv_vAlignTable['u9'] = 'center';gv_vAlignTable['u7'] = 'center';document.getElementById('u17_img').tabIndex = 0;

u17.style.cursor = 'pointer';
$axure.eventManager.click('u17', function(e) {

if (true) {

	SetPanelState('u12', 'pd0u12','fade','',500,'fade','',500);

}
});
document.getElementById('u23_img').tabIndex = 0;

u23.style.cursor = 'pointer';
$axure.eventManager.click('u23', function(e) {

if (true) {

	SetPanelState('u12', 'pd0u12','fade','',500,'fade','',500);

}
});
gv_vAlignTable['u24'] = 'center';gv_vAlignTable['u25'] = 'top';gv_vAlignTable['u28'] = 'center';gv_vAlignTable['u5'] = 'center';gv_vAlignTable['u18'] = 'center';gv_vAlignTable['u19'] = 'top';gv_vAlignTable['u36'] = 'center';gv_vAlignTable['u22'] = 'center';gv_vAlignTable['u33'] = 'top';$('#u34').attr('axSubmit', 'u35');

$axure.eventManager.focus('u34', function(e) {

if ((GetWidgetText('u34')) == ('在此输入密码')) {

SetWidgetFormText('u34', '');

}
});

$axure.eventManager.blur('u34', function(e) {

if ((GetWidgetText('u34')) == ('')) {

SetWidgetFormText('u34', '在此输入密码');

}
});
document.getElementById('u35_img').tabIndex = 0;

u35.style.cursor = 'pointer';
$axure.eventManager.click('u35', function(e) {

if (((GetWidgetText('u32')) == ('xuanchuanbu')) && ((GetWidgetText('u34')) == ('xuanchuanbu'))) {

	SetPanelState('u12', 'pd1u12','fade','',500,'fade','',500);

SetGlobalVariableValue('username', 'xuanchuanbu');

}
else
if (((GetWidgetText('u32')) == ('wenlian')) && ((GetWidgetText('u34')) == ('wenlian'))) {

	SetPanelState('u12', 'pd2u12','fade','',500,'fade','',500);

SetGlobalVariableValue('username', 'wenlian');

}
else
if (((GetWidgetText('u32')) == ('other')) && ((GetWidgetText('u34')) == ('other'))) {

	SetPanelState('u12', 'pd3u12','fade','',500,'fade','',500);

SetGlobalVariableValue('OnLoadVariable', 'other');

}
else
if (true) {

	SetPanelState('u12', 'pd4u12','fade','',500,'fade','',500);
function waitu793df51d50bd4568a983cc26ca0fe19d1() {

	SetPanelState('u12', 'pd0u12','none','',500,'none','',500);
}
setTimeout(waitu793df51d50bd4568a983cc26ca0fe19d1, 1000);

}
});
