package edu.hawaii.its.api.access;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.hawaii.its.api.service.MemberAttributeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private MemberAttributeService memberAttributeService;

    private static final Log logger = LogFactory.getLog(AuthorizationServiceImpl.class);

    /**
     * Assigns roles to user
     *
     * @param uhUuid   The uhUuid of the user.
     * @param username The username of the person to find the user.
     * @return Returns an array list of roles assigned to the user.
     */
    @Override
    public RoleHolder fetchRoles(String uhUuid, String username) {
        RoleHolder roleHolder = new RoleHolder();
        roleHolder.add(Role.ANONYMOUS);
        roleHolder.add(Role.UH);

        if (isOwner(username)) {
            roleHolder.add(Role.OWNER);
        }

        if (isAdmin(username)) {
            roleHolder.add(Role.ADMIN);
        }
        return roleHolder;
    }

    /**
     * Determines if a user is an owner of any grouping.
     *
     * @param username self-explanitory
     * @return true if the person has groupings that they own, otherwise false.
     */
    public boolean isOwner(String username) {
        try {
            if (!memberAttributeService.isOwner(username)) {
                logger.info("This person is an owner");
                return true;
            } else {
                logger.info("This person is not an owner");
            }
        } catch (Exception e) {
            logger.info("The grouping for this person is " + e.getMessage());
        }
        return false;
    }

    /**
     * Determines if a user is an admin in grouping admin.
     *
     * @param username self-explanitory
     * @return true if the person gets pass the grouping admins check by checking if they can get all the groupings.
     */
    public boolean isAdmin(String username) {
        logger.info("isAdmin; username: " + username + ";");
        return memberAttributeService.isAdmin(username);
    }
}
