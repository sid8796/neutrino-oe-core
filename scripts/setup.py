#!/usr/bin/env python3
import argparse,json,os,subprocess
from pathlib import Path
p=argparse.ArgumentParser()
p.add_argument('--build-dir',default='build');p.add_argument('--downloads');p.add_argument('--sstate');p.add_argument('--jobs',type=int)
a=p.parse_args();root=Path(__file__).resolve().parent.parent
for name,data in json.loads((root/'sources.lock.json').read_text()).items():
    if name not in ('bitbake','openembedded-core','meta-openembedded'):continue
    rev=subprocess.check_output(['git','-C',str(root/name),'rev-parse','HEAD'],text=True).strip()
    if rev!=data['rev']:raise SystemExit('Wrong submodule revision: '+name)
build=Path(a.build_dir).expanduser().resolve();conf=build/'conf';conf.mkdir(parents=True,exist_ok=True)
if (conf/'local.conf').exists():raise SystemExit('Build already configured; refusing to overwrite local.conf')
layers=['openembedded-core/meta','meta-openembedded/meta-oe','meta-openembedded/meta-python','meta-openembedded/meta-networking','meta-openembedded/meta-multimedia','meta-formuler','meta-neutrino','meta-softcams']
(conf/'bblayers.conf').write_text('LCONF_VERSION = "7"\nBBPATH = "${TOPDIR}"\nBBFILES = ""\nBBLAYERS = "'+' '.join(str(root/x) for x in layers)+'"\n')
cpus=os.cpu_count() or 1;memory=os.sysconf('SC_PAGE_SIZE')*os.sysconf('SC_PHYS_PAGES')
jobs=a.jobs or max(1,min(cpus,memory//(3*1024**3)))
s='MACHINE = "formuler3"\nDISTRO = "formuler3-ni"\nPACKAGE_CLASSES = "package_ipk"\nCONF_VERSION = "2"\n'
s+=f'BB_NUMBER_THREADS = "{jobs}"\nPARALLEL_MAKE = "-j {cpus}"\n'
s+='INHERIT:remove = "rm_work"\nBB_DISKMON_DIRS = "STOPTASKS,${TMPDIR},10G,100K STOPTASKS,${DL_DIR},10G,100K"\n'
for name,value in [('DL_DIR',a.downloads),('SSTATE_DIR',a.sstate)]:
    if value:
        path=str(Path(value).expanduser().resolve())
        if any(c in path for c in '\n\r"'):raise SystemExit('Invalid cache path')
        s+=f'{name} = "{path}"\n'
(conf/'local.conf').write_text(s)
print('Prepared:',build);print('Next: source scripts/env.sh '+str(build));print('Then: bitbake -p; bitbake ni-formuler3-image')
