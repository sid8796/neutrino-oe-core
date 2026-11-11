# Build the Neutrino timezone menu from the zoneinfo shipped in the image.
IMAGE_INSTALL:append = " tzdata"
ROOTFS_POSTPROCESS_COMMAND:append = " ni_write_timezone_catalog; "

python ni_write_timezone_catalog() {
    from pathlib import Path
    from xml.etree import ElementTree as ET

    root = Path(d.getVar('IMAGE_ROOTFS'))
    zoneinfo = root / d.getVar('datadir').lstrip('/') / 'zoneinfo'
    with (zoneinfo / 'zone.tab').open(encoding='utf-8') as tab:
        zones = {line.split()[2] for line in tab if line.strip() and not line.startswith('#')}

    catalog = ET.Element('timezones')
    # Preserve the initial menu label expected by the Neutrino GUI.
    choices = [
        ('UTC', 'Etc/UTC'),
        ('(GMT+01:00) Amsterdam, Berlin, Bern, Rome, Vienna', 'Europe/Berlin'),
    ]
    choices.extend((zone, zone) for zone in sorted(zones))
    for name, zone in choices:
        if (zoneinfo / zone).is_file():
            ET.SubElement(catalog, 'zone', name=name, zone=zone)

    config = root / d.getVar('sysconfdir').lstrip('/') / 'timezone.xml'
    ET.ElementTree(catalog).write(config, encoding='utf-8', xml_declaration=True)
}
