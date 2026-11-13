# neutrino-oe-core

Самостоятельная сборка NI Neutrino для Formuler F1, F3, F4 и F4 Turbo
на Yocto Scarthgap. Аппаратная адаптация проверялась на F3.

## Три репозитория проекта

| Репозиторий | Назначение |
| --- | --- |
| [neutrino-oe-core](https://github.com/sid8796/neutrino-oe-core) | Сборка, BSP, зависимости, дистрибутив, образ и softcam-рецепты |
| [ni-neutrino / formuler3](https://github.com/sid8796/ni-neutrino/tree/formuler3) | GUI, первый модельный коммит Add Formuler F3 |
| [ni-libstb-hal / formuler3](https://github.com/sid8796/ni-libstb-hal/tree/formuler3) | HAL, регистрация модели и консервативные аппаратные возможности |

GUI и HAL являются настоящими форками neutrino-images с сохранённой историей.
Ветки master содержат upstream; рабочая ветка порта — formuler3, основанная
на ревизиях, использованных в проверенной на ресивере сборке. Рецепты указывают
на конкретные SHA этой ветки. Регистрация F3 хранится в коммитах форков;
внешние патчи регистрации удалены из core. Оставшиеся общие исправления
сборки временно применяются из meta-neutrino.

## Структура

```text
bitbake/                 # закреплённый подмодуль
openembedded-core/       # закреплённый подмодуль
meta-openembedded/       # закреплённый подмодуль; выбранные слои библиотек
meta-formuler/           # BSP для четырёх моделей Formuler
meta-neutrino/           # GUI/HAL-рецепты, общие зависимости, distro и образ
meta-softcams/           # необязательные рецепты OSCam-Emu и CCcam
scripts/
```

Отдельный meta-openpli не нужен. Используемые общие рецепты перенесены
с сохранением происхождения и лицензий: см. PROVENANCE.md.

## Сборка в Linux / WSL2 Ubuntu 24.04

Размещайте checkout внутри Linux home. Установите build-essential, chrpath,
cpio, diffstat, file, gawk, git, locales, python3, python3-pexpect, python3-pip,
python3-jinja2, socat, texinfo, unzip, wget, xz-utils, zstd, liblz4-tool,
libacl1, libssl-dev, zip, gettext, subversion, default-jre-headless.
На build host должна быть доступна en_US.UTF-8.

```sh
git clone --recurse-submodules https://github.com/sid8796/neutrino-oe-core.git
cd neutrino-oe-core
python3 scripts/setup.py --machine formuler3 --build-dir "$HOME/neutrino-build"
source scripts/env.sh "$HOME/neutrino-build"
bitbake -p
bitbake ni-formuler-image
```

setup.py определяет CPU/RAM и ограничивает параллельные задачи по памяти.
Для повторного использования кэша укажите --downloads и --sstate.
Для другой модели укажите `--machine formuler1`, `--machine formuler4` или
`--machine formuler4turbo` и используйте отдельный каталог сборки.
Существующий каталог можно настроить через `MACHINE` в `conf/local.conf`.
Цель `ni-formuler3-image` также доступна при `MACHINE = "formuler3"`.

USB ZIP находится в `tmp/deploy/images/<MACHINE>/`. Для проверки ZIP на F3
используйте `python3 scripts/check_deploy.py /путь/к/архиву.zip`.

В образе: Linux 4.10.6, vendor DVB-модули, NI GUI/HAL, Lua, частичная
русская локаль и шрифты, 88x2bu, Wi-Fi hooks, serial getty и SSH/SFTP.
Личных паролей Wi-Fi и персональных списков каналов в репозитории нет.
FTP-сервер в базовом образе не предустановлен; используйте SFTP на порту 22.
Auto DiSEqC оставлен выключенным по просьбе пользователя.

HAL объявляет один слот CI на каждой из четырёх моделей. Для F1 задан
8-символьный VFD, для F3, F4 и F4 Turbo — четырёхзначный LED. На F3
проверены часы и глубокое выключение из Neutrino, запущенного с USB.
Дисплей F1 и работа CAM-модулей ещё требуют проверки на ресиверах.
Поддержка CEC и PiP пока не подтверждена.
Специфические аудио-исключения других моделей не переносились без проверки F3.
Полная сборка из этого отдельного core завершена 2026-11-04. USB-архив прошёл
проверку структуры, целостности gzip и размеров разделов. Загрузка нового образа
на ресивере ещё не проверялась; результаты проверок — VALIDATION.md.

## Обновления upstream GUI и HAL

Пример для GUI (для HAL замените имя репозитория):

```sh
git clone -b formuler3 https://github.com/sid8796/ni-neutrino.git
cd ni-neutrino
git remote add upstream https://github.com/neutrino-images/ni-neutrino.git
git fetch upstream
git merge upstream/master
# разрешить возможные конфликты и проверить изменения
git push origin formuler3
```

После слияния обновите SRCREV в соответствующем рецепте core и rev в
sources.lock.json. Затем проверьте оставшиеся внешние патчи, разбор BitBake,
сборку и оборудование. Закреплённая сборка не меняется автоматически после push.

## Softcam

OSCam-Emu и CCcam входят в образ `ni-formuler-image` вместе с зависимостями.
Собрать IPK отдельно можно следующими командами.

```sh
bitbake neutrino-plugin-softcams-oscam-emu
bitbake neutrino-plugin-softcams-cccam
```

OSCam: /var/tuxbox/config/oscam-emu/oscam.conf, oscam.server, oscam.user.
Для локального DVBAPI Neutrino задайте в секции [dvbapi] enabled = 1 и pmt_mode = 1.
Управление OSCam доступно в меню «Сервис → Camd control». При запуске образа OSCam стартует, если был включён в меню.
CCcam: /var/keys/CCcam.cfg. Учётные данные не поставляются. Управление:
/etc/init.d/softcam.oscam-emu или /etc/init.d/softcam.cccam.
