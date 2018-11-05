/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package functionaltests.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.ow2.proactive.boot.microservices.iam.IAMStarter;


public class IAMTHelper {

    private static Process iamProcess = null;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                killIAM();
            } catch (Exception ignored) {
            }
        }));
    }

    public static void startIAM(String paHome, String bootMicroservicesPath, String bootConfigurationPath)
            throws InterruptedException, IOException, ExecutionException, ConfigurationException,
            GeneralSecurityException {

        if (iamProcess == null) {

            System.out.println("Starting IAM microservice...");
            iamProcess = IAMStarter.start(paHome, bootMicroservicesPath, bootConfigurationPath);
        }
    }

    private static void killIAM() throws InterruptedException {

        if (iamProcess != null) {
            System.out.println("Stopping IAM microservice...");
            iamProcess.destroy();

            if (!iamProcess.waitFor(1, TimeUnit.MINUTES)) {
                iamProcess.destroyForcibly();
                iamProcess.waitFor(1, TimeUnit.MINUTES);
            }

            iamProcess = null;
        }

    }

}