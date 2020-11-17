#!/bin/bash

thisFileDir="$( cd "$( dirname "${BASH_SOURCE[0]}")" && pwd )"
serviceDir="$( cd "${thisFileDir}/.." && pwd )"


echo "sh  ${serviceDir}/control/start_common.sh"
sh  ${serviceDir}/control/start_common.sh -Dlog_immediateFlush=true
