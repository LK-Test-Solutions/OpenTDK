package org.opentdk.api.dispatcher;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.exception.DataContainerException;
import lombok.Getter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * This class gets used as super class for properties storage. It provides a DataContainer and access methods to reduce the
 * class size in the implementing class. It requires a constructor and a build method where all DispatchComponents get added
 * to the component map for later access.
 * <br><br>
 * Usage example:
 * <pre>
 * public class ETranslation extends MainDispatcher {
 *
 *     public ETranslation(DataContainer dc, boolean createFileFlag, String root) {
 *         super(dc, createFileFlag, root);
 *     }
 *
 *     public void build() {
 *         super.build();
 *         for(EComp comp : EComp.values()) {
 *             getDispatchComponents().put(comp, new DispatchComponent(getDataContainer(), comp.getParamName(), comp.getPxp(), comp.getDVal()));
 *         }
 *     }
 *
 * }
 * </pre>
 *
 * The enumeration that is used here looks like this:
 * <pre>
 * ...
 * KEYWORDMATCH("match", "/translation/keywords[@type='{param_1}']/keyword[@name='{param_2}']", "");
 *
 *     private final String paramName;
 *     private final String pxp;
 *     private final String dVal;
 *
 *     EComp(String paramName, String pxp, String dVal) {
 *         this.paramName = paramName;
 *         this.pxp = pxp;
 *         this.dVal = dVal;
 *     }
 * </pre>
 *
 * This allows to initialize and access properties like this:
 * <pre>
 * ETranslation settings = new ETranslation(notationRulesDC, false, "root");
 * settings.build();
 * ...
 * settings.get(EComp.KEYWORDMATCH).getValue(ATTR_NAME_1 + ";" + ATTR_NAME_2);
 * </pre>
 *
 * For detailed description of the components usage see {@link DispatchComponent}.
 *
 * @author FME (LK Test Solutions)
 */
public abstract class MainDispatcher {

    @Getter
    private final DataContainer dataContainer;
    @Getter
    protected final Map<Enum<?>, DispatchComponent> dispatchComponents;
    private final boolean createFile;
    private final String root;

    public MainDispatcher(DataContainer dc, boolean createFileFlag, String root) {
        this.dataContainer = dc;
        this.createFile = createFileFlag;
        this.root = root;
        this.dispatchComponents = new HashMap<>();
    }

    public void build() {
        if (createFile) {
            if (dataContainer.isTree() && dataContainer.isXML()) {
                if (!dataContainer.xmlInstance().getRootNode().contentEquals(root)) {
                    try {
                        dataContainer.xmlInstance().initXmlEditor(root);
                    } catch (ParserConfigurationException | IOException | SAXException e) {
                        throw new DataContainerException(e);
                    }
                }
            }
//            try {
//                if (Files.notExists(dataContainer.getInputFile())) {
//                    dataContainer.createFile();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public DispatchComponent get(Enum<?> key) {
        return dispatchComponents.get(key);
    }
}
